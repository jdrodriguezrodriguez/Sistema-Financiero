package com.banco.sistemabancario.entity;

import java.util.HashSet;
import java.util.Set;

import com.banco.sistemabancario.entity.enums.RoleEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table; 

@Entity
@Table(name = "roles")
public class Roles {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "roles_permisos", joinColumns =  @JoinColumn(name = "roles_id"), inverseJoinColumns = @JoinColumn(name = "permisos_id"))
    private Set<Permisos> permisosList = new HashSet<>();

    // Constructor sin argumentos requerido por Hibernate
    public Roles() {}

    public Roles(Integer id, RoleEnum roleEnum, Set<Permisos> permisosList) {
        this.id = id;
        this.roleEnum = roleEnum;
        this.permisosList = permisosList;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public RoleEnum getRoleEnum() {
        return roleEnum;
    }
    public void setRoleEnum(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }

    public Set<Permisos> getPermisosList() {
        return permisosList;
    }
    public void setRoles(Set<Permisos> permisosList) {
        this.permisosList = permisosList;
    }
}
