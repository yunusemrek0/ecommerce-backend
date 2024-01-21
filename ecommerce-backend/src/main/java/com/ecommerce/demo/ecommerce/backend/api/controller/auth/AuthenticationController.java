package com.ecommerce.demo.ecommerce.backend.api.controller.auth;

import com.ecommerce.demo.ecommerce.backend.api.model.LoginBody;
import com.ecommerce.demo.ecommerce.backend.api.model.LoginResponse;
import com.ecommerce.demo.ecommerce.backend.api.model.RegistrationBody;
import com.ecommerce.demo.ecommerce.backend.exception.EmailFailureException;
import com.ecommerce.demo.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.demo.ecommerce.backend.exception.UserNotVerifiedException;
import com.ecommerce.demo.ecommerce.backend.model.LocalUser;
import com.ecommerce.demo.ecommerce.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerUser(@Valid @RequestBody RegistrationBody registrationBody) throws EmailFailureException {
        userService.registerUser(registrationBody);

        Map<String,String> map = new HashMap<>();
        map.put("message","User has been saved successfully");

        return ResponseEntity.ok(map);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginBody loginBody) throws ResourceNotFoundException, UserNotVerifiedException, EmailFailureException {

        LoginResponse jwt = userService.loginUser(loginBody);

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token) {
        if (userService.verifyUser(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user){

        return user;

    }
}
