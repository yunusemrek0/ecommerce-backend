package com.ecommerce.demo.ecommerce.backend.service;

import com.ecommerce.demo.ecommerce.backend.exception.EmailFailureException;
import com.ecommerce.demo.ecommerce.backend.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    @Value("{email.form}")
    private String fromAddress;

    @Value("{app.fronted.url}")
    private String url;

    @Autowired
    private JavaMailSender javaMailSender;


    private SimpleMailMessage makeMailMessage(){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);

        return simpleMailMessage;
    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
        SimpleMailMessage message = makeMailMessage();
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("Please verify your email which is sent");
        message.setText("Please follow the steps below than verify your account.\n" +
                url +"/auth/verify?token=" + verificationToken.getToken());

        try {
            javaMailSender.send(message);
        }catch (MailException e){
            throw new EmailFailureException();

        }
    }
}
