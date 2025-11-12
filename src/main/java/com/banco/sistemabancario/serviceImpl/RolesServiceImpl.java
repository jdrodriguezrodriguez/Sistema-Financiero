package com.banco.sistemabancario.serviceImpl;

import org.springframework.stereotype.Service;

import com.banco.sistemabancario.entity.Roles;
import com.banco.sistemabancario.entity.enums.RoleEnum;
import com.banco.sistemabancario.repository.RolesRepository;
import com.banco.sistemabancario.service.RolesService;

@Service
public class RolesServiceImpl implements RolesService{

    private RolesRepository rolesRepository;
    public RolesServiceImpl(RolesRepository rolesRepository){
        this.rolesRepository = rolesRepository;
    }

    @Override
    public Roles buscarRoles(RoleEnum roleEnum){
        return rolesRepository.findByRoleEnum(roleEnum)
        .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
    }
}
