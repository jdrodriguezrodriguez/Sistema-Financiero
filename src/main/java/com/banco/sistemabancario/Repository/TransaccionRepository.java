
package com.banco.sistemabancario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.Entity.Transaccion;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer>{
    
}