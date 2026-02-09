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
@NoArgsConstructor
public class LoginFailure {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_login")
    private Integer idLogin;

    private int id_usuario;

    @CurrentTimestamp
    private LocalDateTime attempted_at;
    
    private String ip_address;
}

