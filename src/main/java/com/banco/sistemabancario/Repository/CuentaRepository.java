package com.banco.sistemabancario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.Entity.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Integer>{
    Cuenta save(Cuenta cuenta);
    boolean existsByNumCuenta(String num_cuenta);
}
