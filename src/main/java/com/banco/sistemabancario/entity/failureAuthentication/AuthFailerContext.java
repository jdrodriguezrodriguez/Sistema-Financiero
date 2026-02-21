package com.banco.sistemabancario.entity.failureAuthentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthFailerContext {
    
    private String username;
    private String ip_address;

    public AuthFailerContext(){
    }
    
    public AuthFailerContext(String username, String ip_address) {
        this.username = username;
        this.ip_address = ip_address;
    }
}
