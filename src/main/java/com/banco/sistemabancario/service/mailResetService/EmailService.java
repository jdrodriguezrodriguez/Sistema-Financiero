package com.banco.sistemabancario.service.mailResetService;

import com.banco.sistemabancario.entity.Transaccion;

public interface EmailService {
    void enviarResetPassword(String email, String token);
    void enviarUsername(String email, String username);

    void notificarTransaccion(Transaccion transaccion, String cuentaEmisor, String cuentaReceptor);

    void enviarInfoTransaccion(String correo, String mensaje);
    void enviarInfoDeposito(Transaccion transaccion, int idUser);

    void enviarTokenRegistro(String nombreCompleto, String email, String token);
}
