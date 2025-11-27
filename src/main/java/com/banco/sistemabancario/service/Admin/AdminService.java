package com.banco.sistemabancario.service.Admin;

import com.banco.sistemabancario.dto.Admin.ActualizarUsuarioAdmin;
import com.banco.sistemabancario.dto.Admin.ConsultarUsuarioAdmin;

public interface AdminService {
    void adminActualizarUsuario(ActualizarUsuarioAdmin datos);
    ConsultarUsuarioAdmin adminBuscarUsuario(String documento);
}
