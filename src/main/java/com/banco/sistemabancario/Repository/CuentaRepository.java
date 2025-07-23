package com.banco.sistemabancario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.Entity.Cuenta;
import com.banco.sistemabancario.Entity.Usuario;

public interface CuentaRepository extends JpaRepository<Cuenta, Integer>{
    Cuenta save(Cuenta cuenta);
    boolean existsByNumCuenta(String numCuenta);
    Cuenta findByUsuario(Usuario usuario);
}
