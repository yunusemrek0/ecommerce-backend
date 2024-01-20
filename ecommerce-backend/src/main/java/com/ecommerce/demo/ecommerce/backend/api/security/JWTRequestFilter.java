package com.ecommerce.demo.ecommerce.backend.api.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.ecommerce.demo.ecommerce.backend.model.LocalUser;
import com.ecommerce.demo.ecommerce.backend.model.dao.LocalUserDAO;
import com.ecommerce.demo.ecommerce.backend.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private LocalUserDAO localUserDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader=request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            String token = tokenHeader.substring(7);
           try{
               String username = jwtService.getUsername(token);
               Optional<LocalUser> opUser = localUserDAO.findByUsername(username);
               if (opUser.isPresent()){
                   LocalUser user = opUser.get();
                   UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,null, new ArrayList<>());
                   authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authentication);
               }
           }catch (JWTDecodeException e){

           }
        }
        filterChain.doFilter(request,response);
    }
}
