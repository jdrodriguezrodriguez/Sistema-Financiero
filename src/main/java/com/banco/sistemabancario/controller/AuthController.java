package com.banco.sistemabancario.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@PreAuthorize("denyAll()")                  //FUNCIONA CON @EnableMethodSecurity
public class AuthController {
    
    @GetMapping("/hello")
    @PreAuthorize("permitAll()") 
    public String hello(){
        return "hello word";
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/hello-secured")
    public String helloSecured(){
        return "hello word";
    }
}
