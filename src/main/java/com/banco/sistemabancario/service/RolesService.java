package com.banco.sistemabancario.service;

import com.banco.sistemabancario.entity.Roles;
import com.banco.sistemabancario.entity.enums.RoleEnum;

public interface RolesService {

    Roles buscarRoles(RoleEnum rol);
}
