package com.banco.sistemabancario.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.failureAuthentication.AuthFailerContext;
import com.banco.sistemabancario.entity.failureAuthentication.SeguridadUsuario;
import com.banco.sistemabancario.repository.LoginFailureRepository;
import com.banco.sistemabancario.repository.SeguridadUsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class LoginFailureServiceTest {

    @Mock
    private LoginFailureRepository loginFailureRepository;

    @Mock
    private SeguridadUsuarioRepository seguridadUsuarioRepository;

    @Mock
    private UsuarioServiceImpl usuarioServiceImpl;

    @InjectMocks
    private LoginFailureServiceImpl loginFailureServiceImpl;

    @Test
    public void desbloqueoUserFailureAuthentication(){
        Usuario usuario = new Usuario();
        usuario.setUsername("root");
        usuario.setIdUsuario(1);

        SeguridadUsuario seguridadUsuario = new SeguridadUsuario();
        seguridadUsuario.setIdUsuarioLog(1);
        seguridadUsuario.setFailed_attempts(3);
        seguridadUsuario.setLock_count(1);
        seguridadUsuario.setLocked_until(LocalDateTime.of(2026, 02, 25, 1, 20));

        when(usuarioServiceImpl.obtenerUsuarioPorUsername("root")).thenReturn(usuario);
        when(seguridadUsuarioRepository.findById(1)).thenReturn(Optional.of(seguridadUsuario));

        boolean validar = loginFailureServiceImpl.desbloqueoUserFailureAuthentication("root");

        ArgumentCaptor<SeguridadUsuario> captor = ArgumentCaptor.forClass(SeguridadUsuario.class);
        verify(seguridadUsuarioRepository, times(1)).save(captor.capture());

        SeguridadUsuario seguridadUsuarioTest = captor.getValue();

        assertEquals(0, seguridadUsuarioTest.getFailed_attempts());
        assertNull(seguridadUsuarioTest.getLocked_until());
        assertTrue(validar);
    }

    @Test
    public void bloquearUsuario_CuandoAlcanzaMaximoIntentos(){
        AuthFailerContext aFailerContext = new AuthFailerContext();
        aFailerContext.setUsername("root");
        aFailerContext.setIp_address("123.123.132");

        Usuario usuario = new Usuario();
        usuario.setUsername("root");
        usuario.setIdUsuario(1);

        SeguridadUsuario seguridadUsuario = new SeguridadUsuario();
        seguridadUsuario.setIdUsuarioLog(1);
        seguridadUsuario.setFailed_attempts(2);
        seguridadUsuario.setLock_count(0);

        when(usuarioServiceImpl.obtenerUsuarioPorUsername("root")).thenReturn(usuario);
        when(seguridadUsuarioRepository.findById(1)).thenReturn(Optional.of(seguridadUsuario));

        boolean validar = loginFailureServiceImpl.registrarFalloLogin(aFailerContext);
        
        ArgumentCaptor<SeguridadUsuario> captor = ArgumentCaptor.forClass(SeguridadUsuario.class);
        verify(seguridadUsuarioRepository, times(1)).save(captor.capture());

        SeguridadUsuario seguridadUsuarioTest = captor.getValue();
        assertEquals(3, seguridadUsuarioTest.getFailed_attempts());
        assertEquals(1, seguridadUsuarioTest.getLock_count());
        assertNotNull(seguridadUsuarioTest.getLocked_until());

        assertTrue(validar);
    }

    @Test
    public void registrarFalloLogin_Existente() {

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

    @Test
    public void registrarFalloLogin_NoExistente() {

        AuthFailerContext aFailerContext = new AuthFailerContext();
        aFailerContext.setUsername("root");

        Usuario usuario = new Usuario();
        usuario.setUsername("root");
        usuario.setIdUsuario(1);

        when(usuarioServiceImpl.obtenerUsuarioPorUsername("root")).thenReturn(usuario);
        when(seguridadUsuarioRepository.findById(1)).thenReturn(Optional.empty());

        boolean validar =  loginFailureServiceImpl.registrarFalloLogin(aFailerContext);
        
        ArgumentCaptor<SeguridadUsuario> captor = ArgumentCaptor.forClass(SeguridadUsuario.class);
        verify(seguridadUsuarioRepository).save(captor.capture());

        SeguridadUsuario seguridadUsuarioTest = captor.getValue();
        assertEquals(1, seguridadUsuarioTest.getIdUsuarioLog());
        assertFalse(validar);
    }
}
