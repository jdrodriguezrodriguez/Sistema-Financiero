package com.banco.sistemabancario.service.admin;

import com.banco.sistemabancario.dto.admin.ActualizarEstadoAdmin;
import com.banco.sistemabancario.dto.admin.ActualizarUsuarioAdmin;
import com.banco.sistemabancario.dto.admin.ConsultarUsuarioAdmin;
import com.banco.sistemabancario.dto.admin.CrearUsuarioAdmin;

public interface AdminService {
    void adminActualizarUsuario(ActualizarUsuarioAdmin datos);
    ConsultarUsuarioAdmin adminBuscarUsuario(String documento);
    void adminCrearUsuario(CrearUsuarioAdmin datos);
    void adminActualizarEstado(ActualizarEstadoAdmin datos);
    void adminEliminarUsuario(String documento);
}
