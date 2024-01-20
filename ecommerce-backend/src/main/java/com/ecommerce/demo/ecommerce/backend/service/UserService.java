package com.ecommerce.demo.ecommerce.backend.service;

import com.ecommerce.demo.ecommerce.backend.api.model.LoginBody;
import com.ecommerce.demo.ecommerce.backend.api.model.LoginResponse;
import com.ecommerce.demo.ecommerce.backend.api.model.RegistrationBody;
import com.ecommerce.demo.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.demo.ecommerce.backend.exception.UserAlreadyExistsException;
import com.ecommerce.demo.ecommerce.backend.model.LocalUser;
import com.ecommerce.demo.ecommerce.backend.model.dao.LocalUserDAO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private JWTService jwtService;

    public LocalUser registerUser( RegistrationBody registrationBody){

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

        localUserDAO.save(user);
        return user;
    }

    public LoginResponse loginUser(LoginBody loginBody) throws ResourceNotFoundException {
        Optional<LocalUser> opUser = localUserDAO.findByUsername(loginBody.getUsername());
        if (opUser.isPresent()){

            LocalUser user = opUser.get();

            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())){

                LoginResponse response = new LoginResponse();
                response.setJwt(jwtService.generateJWT(user));
                return response;

            }

        }else {
            throw new ResourceNotFoundException("User cannot found with username : "+loginBody.getUsername());

        }
        return null;
    }
}
