package com.banco.sistemabancario.dto;

public class AuthFailerContext {
    
    private String username;
    private String ip_address;

    public AuthFailerContext(){
    }
    
    public AuthFailerContext(String username, String ip_address) {
        this.username = username;
        this.ip_address = ip_address;
    }

    public String getUsername() {
        return username;
    }
    public String getIp_address() {
        return ip_address;
    }
}
