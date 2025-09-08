package com.banco.sistemabancario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @OneToOne
    @JoinColumn(name = "idPersona", referencedColumnName = "idPersona")
                        //Usuario//                         //Persona//
    private Persona persona;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol;

    public Usuario(){
    }

    public Usuario(int idUsuario, String username, String password, String rol, Persona persona){
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.persona = persona;
    }

    public Integer getIdUsuario(){
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario){
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

    public Persona getPersona() {
        return persona;
    }
    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}