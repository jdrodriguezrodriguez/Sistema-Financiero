package com.banco.sistemabancario.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @OneToOne           //Usuario//                         //Persona//
    @JoinColumn(name = "idPersona", referencedColumnName = "idPersona")
                        
    private Persona persona;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol;
    

    /* */
    /* */
    /* */
    //NUEVA IMPLEMENTACION, AGREGAR A LA BASE DE DATOS:
    /* */
    /* */
    /* */

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_no_expired")
    private boolean accountNoExpired;

    @Column(name = "account_no_locked")
    private boolean accountNoLocked;

    @Column(name = "credential_no_expired")
    private boolean credentialNoExpired;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)        //CARGAR TODOS LOS ROLES DEL USUARIO
    @JoinTable(name = "user_roles", joinColumns =  @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles = new HashSet<>();

    /* */
    /* */
    /* */
    
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