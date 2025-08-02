
package com.banco.sistemabancario.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.Entity.Cuenta;
import com.banco.sistemabancario.Entity.Transaccion;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer>{
    List<Transaccion> findByCuenta(Cuenta cuenta);
}