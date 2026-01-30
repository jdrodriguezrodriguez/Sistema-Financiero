package com.banco.sistemabancario.entity.MailReset;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.banco.sistemabancario.entity.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "register_token")
public class RegisterToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_register_token")
    private Integer idRegister;

    @Column(name = "token")
    private String token;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    @Column(name = "expiracion ")
    private LocalDateTime expiracion;

    @Column(name = "uso")
    private boolean uso;

    @Column(name = "creacion ")
    @CreationTimestamp
    private LocalDateTime creacion;
}
