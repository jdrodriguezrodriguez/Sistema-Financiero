package com.banco.sistemabancario.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_cuenta")
    private Integer num_cuenta;

    @OneToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(nullable = false)
    private String estado;

    public Cuenta(){
    }
    
    public Cuenta(Integer num_cuenta, Usuario usuario, BigDecimal saldo, String estado) {
        this.num_cuenta = num_cuenta;
        this.usuario = usuario;
        this.saldo = saldo;
        this.estado = estado;
    }

    public Integer getNum_cuenta() {
        return num_cuenta;
    }
    public void setNum_cuenta(Integer num_cuenta) {
        this.num_cuenta = num_cuenta;
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
