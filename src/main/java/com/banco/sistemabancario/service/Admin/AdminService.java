package com.banco.sistemabancario.service.Admin;

import com.banco.sistemabancario.dto.Admin.ActualizarEstadoAdmin;
import com.banco.sistemabancario.dto.Admin.ActualizarUsuarioAdmin;
import com.banco.sistemabancario.dto.Admin.ConsultarUsuarioAdmin;
import com.banco.sistemabancario.dto.Admin.CrearUsuarioAdmin;

public interface AdminService {
    void adminActualizarUsuario(ActualizarUsuarioAdmin datos);
    ConsultarUsuarioAdmin adminBuscarUsuario(String documento);
    void adminCrearUsuario(CrearUsuarioAdmin datos);
    void adminActualizarEstado(ActualizarEstadoAdmin datos);
    void adminEliminarUsuario(String documento);
}
