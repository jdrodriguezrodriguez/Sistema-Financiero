package com.banco.sistemabancario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.Cuenta;
import com.banco.sistemabancario.entity.Usuario;

public interface CuentaRepository extends JpaRepository<Cuenta, String>{
    boolean existsByNumCuenta(String numCuenta);
    Cuenta findByUsuario(Usuario usuario);
    Optional<Cuenta> findByUsuario_IdUsuario(int idUser);
    Cuenta findByUsuario_Persona_IdPersona(int idPersona);
}
