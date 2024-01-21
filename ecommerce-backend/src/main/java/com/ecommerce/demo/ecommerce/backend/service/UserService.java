package com.ecommerce.demo.ecommerce.backend.service;

import com.ecommerce.demo.ecommerce.backend.api.model.LoginBody;
import com.ecommerce.demo.ecommerce.backend.api.model.LoginResponse;
import com.ecommerce.demo.ecommerce.backend.api.model.RegistrationBody;
import com.ecommerce.demo.ecommerce.backend.exception.EmailFailureException;
import com.ecommerce.demo.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.demo.ecommerce.backend.exception.UserAlreadyExistsException;
import com.ecommerce.demo.ecommerce.backend.exception.UserNotVerifiedException;
import com.ecommerce.demo.ecommerce.backend.model.LocalUser;
import com.ecommerce.demo.ecommerce.backend.model.VerificationToken;
import com.ecommerce.demo.ecommerce.backend.model.dao.LocalUserDAO;
import com.ecommerce.demo.ecommerce.backend.model.dao.VerificationTokenDAO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenDAO verificationTokenDAO;

    public LocalUser registerUser( RegistrationBody registrationBody) throws EmailFailureException {

        if (localUserDAO.findByEmail(registrationBody.getEmail()).isPresent() ||
            localUserDAO.findByUsername(registrationBody.getUsername()).isPresent() ){

            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();

        user.setEmail(registrationBody.getEmail());
        user.setUsername(registrationBody.getUsername());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());

        // TODO: Encrypt passwords !!
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        verificationTokenDAO.save(verificationToken);




        localUserDAO.save(user);
        return user;
    }

    public LoginResponse loginUser(LoginBody loginBody) throws ResourceNotFoundException,  EmailFailureException {
        Optional<LocalUser> opUser = localUserDAO.findByUsername(loginBody.getUsername());
        if (opUser.isPresent()){

            LocalUser user = opUser.get();

            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())){

                if (user.getIsEmailVerified()){

                    LoginResponse response = new LoginResponse();
                    response.setJwt(jwtService.generateJWT(user));
                    response.setSuccess(true);
                    return response;

                }else {

                    List<VerificationToken> verificationTokens = user.getVerificationTokens();

                    boolean resend = verificationTokens.isEmpty() ||
                            verificationTokens.get(0).getCreatedTimestamp().before(new Timestamp(System.currentTimeMillis() - (60*60*1000)));

                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenDAO.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }

                    try {
                        throw new UserNotVerifiedException(resend);
                    } catch (UserNotVerifiedException e) {
                        LoginResponse loginResponse = new LoginResponse();
                        loginResponse.setSuccess(false);
                        String reason = "USER NOT VERIFIED";

                        if (e.isNewEmailSent()){
                            reason += " EMAIL RESENT";
                        }
                        loginResponse.setFailureReason(reason);

                        throw new RuntimeException(e);
                    }
                }

            }

        }else {
            throw new ResourceNotFoundException("User cannot found with username : "+loginBody.getUsername());

        }
        return null;
    }

    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateEmailVerificationJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> opToken = verificationTokenDAO.findByToken(token);
        if (opToken.isPresent()){
            VerificationToken verificationToken = opToken.get();
            LocalUser user = verificationToken.getUser();
            if (!user.getIsEmailVerified()){
                user.setIsEmailVerified(true);
                localUserDAO.save(user);
                verificationTokenDAO.deleteByUser(user);
                return true;
            }
        }
        return false;
    }
}
