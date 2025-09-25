package com.banco.sistemabancario.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuenta")
public class Cuenta {

    @Id
    @Column(name = "num_cuenta")
    private String numCuenta;

    @OneToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "cuenta")     //Atributo en la clase Transaccion//
    private List<Transaccion> transacciones;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(nullable = false)
    private String estado;

    public Cuenta(){
    }
    
    public Cuenta(String numCuenta, Usuario usuario, BigDecimal saldo, String estado) {
        this.numCuenta = numCuenta;
        this.usuario = usuario;
        this.saldo = saldo;
        this.estado = estado;
    }

    public String getNum_cuenta() {
        return numCuenta;
    }
    public void setNum_cuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
