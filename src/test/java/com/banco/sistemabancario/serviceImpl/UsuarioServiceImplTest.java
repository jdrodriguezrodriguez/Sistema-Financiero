package com.banco.sistemabancario.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.entity.Permisos;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Roles;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.enums.RoleEnum;
import com.banco.sistemabancario.exception.UsuarioNoencontradoException;
import com.banco.sistemabancario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RolesServiceImpl rolesServiceImpl;

    @InjectMocks
    private UsuarioServiceImpl usuarioServiceImpl;

    @Test
    public void LanzarExcepcion_ActualizarDatosUsuario(){

        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoencontradoException.class, () -> {
            usuarioServiceImpl.actualizarDatosUsuario(null, 1);
        });

        verify(usuarioRepository, times(0)).save(any());
    }

    @Test
    public void actualizarDatosUsuario(){
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);

        String password = "1234";

        ActualizarUsuarioDto datos = new ActualizarUsuarioDto("root", password);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(anyString())).thenReturn(password);

        usuarioServiceImpl.actualizarDatosUsuario(datos, 1);

        ArgumentCaptor <Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario usuarioTest = captor.getValue();
        assertEquals("root", usuarioTest.getUsername());
        assertEquals(password, usuarioTest.getPassword());

        verify(usuarioRepository).findById(1);
        verify(passwordEncoder).encode(password);
    }

    @Test
    public void registrarUsuario(){
        Persona persona = new Persona(
            "juan", 
            "rodriguez", 
            "jjuanrodriguex@gmail.com", 
            "1110450635", 
            Date.valueOf(LocalDate.of(2004, Month.AUGUST, 23)));
            
        Permisos permisos = new Permisos("READ");
        Roles roles = new Roles(RoleEnum.CLIENTE, Set.of(permisos));

        String pasString = "1234";
        when(passwordEncoder.encode(anyString())).thenReturn(pasString);

        when(rolesServiceImpl.buscarRoles(RoleEnum.CLIENTE)).thenReturn(roles);

        usuarioServiceImpl.registrarUsuario("juan", "rodriguez", "1234", persona);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository,times(1)).save(captor.capture());
        Usuario usuarioTest = captor.getValue();

        assertEquals("juanro", usuarioTest.getUsername());
        assertEquals("1234", usuarioTest.getPassword());

        verify(passwordEncoder).encode("1234");
    }

    @Test
    public void adminRegistrarUsuario(){
        Persona persona = new Persona();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setUsername("root");

        Permisos permisos = new Permisos("CREATE");
        Roles rol = new Roles(RoleEnum.ADMIN, Set.of(permisos));

        String pasString = "1234";

        /* when(usuarioRepository.findByUsername("root")).thenReturn(Optional.empty()); */
        when(passwordEncoder.encode(anyString())).thenReturn(pasString);
        when(rolesServiceImpl.buscarRoles(RoleEnum.ADMIN)).thenReturn(rol);

        usuarioServiceImpl.adminRegistrarUsuario("root", "1234", persona, "ADMIN", "ADMIN");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository, times(1)).save(captor.capture());
        Usuario usuarioTest = captor.getValue();
        assertEquals("root", usuarioTest.getUsername());
    }
    
    @Test
    public void DesbloqueoUserFailureAuthentication(){
        Usuario usuario = new Usuario();
        usuario.setUsername("root");
        usuario.setAccountNoLocked(false);

        when(usuarioRepository.findByUsername("root")).thenReturn(Optional.of(usuario));

        usuarioServiceImpl.DesbloqueoUserFailureAuthentication("root");

        assertTrue(usuario.isAccountNoLocked());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    public void LanzarExcepcion_DesbloqueoUserFailureAuthentication(){

        when(usuarioRepository.findByUsername("root")).thenReturn(Optional.empty());

        assertThrows(UsuarioNoencontradoException.class, () ->{
            usuarioServiceImpl.DesbloqueoUserFailureAuthentication("root");
        });

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void LanzarExcepcion_ValidarNombreUsuario(){
        
        Usuario usuarioExiste = new Usuario();
        usuarioExiste.setUsername("root");
        usuarioExiste.setIdUsuario(2);

        when(usuarioRepository.findByUsername("root")).thenReturn(Optional.of(usuarioExiste));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () ->
            usuarioServiceImpl.validarNombreUsuario("root", 1)
        );

        assertTrue(exception.getMessage().contains("El usuario ya existe, cambiar username"));
    }

    @Test
    public void NoLanzarExcepcion_ValidarNombreUsuario(){
        when(usuarioRepository.findByUsername("root")).thenReturn(Optional.empty());

        assertDoesNotThrow(
            () -> usuarioServiceImpl.validarNombreUsuario("root", 1));
    }
}
