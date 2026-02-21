package com.banco.sistemabancario.serviceImpl;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.failureAuthentication.AuthFailerContext;
import com.banco.sistemabancario.entity.failureAuthentication.SeguridadUsuario;
import com.banco.sistemabancario.repository.SeguridadUsuarioRepository;



@ExtendWith(MockitoExtension.class)
public class LoginFailureServiceTest {

    @Mock
    private SeguridadUsuarioRepository seguridadUsuarioRepository;

    @Mock
    private UsuarioServiceImpl usuarioServiceImpl;

    @InjectMocks
    private LoginFailureServiceImpl loginFailureServiceImpl;

    @Test
    void registrarFalloLogin() {

        AuthFailerContext aFailerContext = new AuthFailerContext();
        aFailerContext.setUsername("root");

        Usuario usuario = new Usuario();
        usuario.setUsername("root");
        usuario.setIdUsuario(1);

        SeguridadUsuario seguridadUsuario = new SeguridadUsuario();
        seguridadUsuario.setIdUsuarioLog(1);
        seguridadUsuario.setFailed_attempts(0);

        when(usuarioServiceImpl.obtenerUsuarioPorUsername("root")).thenReturn(usuario);
        when(seguridadUsuarioRepository.findById(1)).thenReturn(Optional.of(seguridadUsuario));
        
        loginFailureServiceImpl.registrarFalloLogin(aFailerContext);

        verify(seguridadUsuarioRepository).save(seguridadUsuario);
    }
}
