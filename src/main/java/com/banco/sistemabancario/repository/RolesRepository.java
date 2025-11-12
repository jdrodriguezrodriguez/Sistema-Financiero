package com.banco.sistemabancario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.Roles;
import com.banco.sistemabancario.entity.enums.RoleEnum;


public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByRoleEnum(RoleEnum roleEnum);
}
