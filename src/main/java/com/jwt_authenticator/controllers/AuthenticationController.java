package com.jwt_authenticator.controllers;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jwt_authenticator.models.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private RestTemplate rest;

    AuthenticationManager authManager;

    public AuthenticationController(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    /**
     * FOR USER AUTHENTICATION
     *
     * @param u
     * @return
     */
    @PostMapping("/Login")
    public String Login(@RequestBody User u) {
        try {
            //Validate user credentials
            if (u.getUser().toLowerCase().trim().equals("admin")
                    && u.getPassword().toLowerCase().trim().equals("admin")) {
                Authentication auth = authManager
                        .authenticate(new UsernamePasswordAuthenticationToken(u.getUser(), u.getPassword()));
                return auth.isAuthenticated() ? getToken(auth) : "no autenticado";

            } else {
                return "user is invalid";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    @PostMapping("/removeToken")
    public String removeToken(@RequestBody User u) {
        try {
            //Validate user credentials
            if (u.getUser().toLowerCase().trim().equals("admin")
                    && u.getPassword().toLowerCase().trim().equals("admin")) {
                Authentication auth = authManager
                        .authenticate(new UsernamePasswordAuthenticationToken(u.getUser(), u.getPassword()));
                
                auth.setAuthenticated(false);
               
                return "token has removed";

            } else {
                return "user is invalid";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * EXAMPLE TO CONSUME ANOTHER MICROSERVICE
     *
     * @return
     */
    @GetMapping("/persons")
    public Object getPersons() {
        return rest.getForObject("http://localhost:8082/tuca_test/persons/v2/list", Object.class);
    }
    

    /**
     * method used to get token
     *
     * @param autentication
     * @return
     */
    private String getToken(Authentication autentication) {
        /* 
		 * the token body includes the user
		 * and the roles to which it belongs, in addition
		 * of the expiration date and the signature data
         */
        String token = Jwts.builder().setIssuedAt(new Date()) // creation date
                .setSubject(autentication.getName()) // user
                .claim("authorities", autentication.getAuthorities().stream() // roles
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setExpiration(new Date(System.currentTimeMillis() + 86_400_000)) // expired date
                .signWith(SignatureAlgorithm.HS512, "TUC@S3cur1ty?GT")// key and algorithm for signature
                .compact(); // token generation
        return token;
    }
    
    
    
}
