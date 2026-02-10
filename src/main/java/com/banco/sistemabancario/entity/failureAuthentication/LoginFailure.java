package com.banco.sistemabancario.entity.failureAuthentication;

import java.time.LocalDateTime;

import org.hibernate.annotations.CurrentTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "login_failure")
@Entity
@Getter
@Setter
@AllArgsConstructor
public class LoginFailure {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_login")
    private Integer idLogin;

    private int id_usuario;

    @CurrentTimestamp
    private LocalDateTime attempted_at;
    
    private String ip_address;

    public LoginFailure(){
    }

    public LoginFailure(int id_usuario, String ip_address) {
        this.id_usuario = id_usuario;
        this.ip_address = ip_address;
    }
}

