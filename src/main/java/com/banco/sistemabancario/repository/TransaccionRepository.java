
package com.banco.sistemabancario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Transaccion;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer>{
    List<Transaccion> findByCuenta(Cuenta cuenta);
}