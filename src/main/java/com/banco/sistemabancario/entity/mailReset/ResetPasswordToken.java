package com.banco.sistemabancario.entity.mailReset;

import java.time.LocalDateTime;

import com.banco.sistemabancario.entity.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "password_reset")
public class ResetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReset")
    private Integer idReset;

    @Column(nullable = false, unique = true, name = "token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime expiracion;

    @Column(name = "uso")
    private boolean uso;

    @Column(name = "creacion")
    private String creacion;
}
