package com.banco.sistemabancario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.Entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByUsername(String username);
    Usuario save(Usuario usuario);
} 
