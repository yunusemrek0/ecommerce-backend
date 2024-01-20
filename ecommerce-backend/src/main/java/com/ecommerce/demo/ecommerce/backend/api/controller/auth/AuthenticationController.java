package com.ecommerce.demo.ecommerce.backend.api.controller.auth;

import com.ecommerce.demo.ecommerce.backend.api.model.LoginBody;
import com.ecommerce.demo.ecommerce.backend.api.model.LoginResponse;
import com.ecommerce.demo.ecommerce.backend.api.model.RegistrationBody;
import com.ecommerce.demo.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.demo.ecommerce.backend.model.LocalUser;
import com.ecommerce.demo.ecommerce.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Map<String,String>> registerUser(@Valid @RequestBody RegistrationBody registrationBody){
        userService.registerUser(registrationBody);

        Map<String,String> map = new HashMap<>();
        map.put("message","User has been saved successfully");

        return ResponseEntity.ok(map);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginBody loginBody) throws ResourceNotFoundException {

        LoginResponse jwt = userService.loginUser(loginBody);

        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user){

        return user;

    }
}
