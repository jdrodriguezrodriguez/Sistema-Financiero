package com.banco.sistemabancario.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banco.sistemabancario.dto.ActualizarUsuarioDto;
import com.banco.sistemabancario.entity.Persona;
import com.banco.sistemabancario.entity.Roles;
import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.entity.enums.RoleEnum;
import com.banco.sistemabancario.entity.enums.TipoEnum;
import com.banco.sistemabancario.exception.PasswordInvalidaException;
import com.banco.sistemabancario.exception.UsuarioNoencontradoException;
import com.banco.sistemabancario.repository.PersonaRepository;
import com.banco.sistemabancario.repository.UsuarioRepository;
import com.banco.sistemabancario.service.RolesService;
import com.banco.sistemabancario.service.UsuarioService;
import com.banco.sistemabancario.util.UsuarioUtils;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private static final Logger logger =  LoggerFactory.getLogger(UsuarioServiceImpl.class);
    
    private UsuarioUtils usuarioUtils;

    private UsuarioRepository usuarioRepository;
    private PersonaRepository personaRepository;
    private RolesService rolesService;
    private PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, 
                            PersonaRepository personaRepository, 
                            RolesService rolesService,
                            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.rolesService = rolesService;
        this.passwordEncoder = passwordEncoder;
    }       

    @Transactional
    @Override
    public Usuario actualizarDatosUsuario(ActualizarUsuarioDto datos, int idUsuario){
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new UsuarioNoencontradoException("No se encontro el usuario"));

        validarNombreUsuario(datos.getUsername(), usuario.getIdUsuario());

        usuario.setUsername(datos.getUsername());
        usuario.setPassword(passwordEncoder.encode(datos.getPassword()));

        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> obtenerUsuarios(){
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorPersonaId(int idPersona){
        return usuarioRepository.findByPersona_IdPersona(idPersona);
    }

    @Override
    public Persona obtenerPersonaPorUsuarioId(int idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new UsuarioNoencontradoException("No se encontro el usuario con ID: " + idUsuario));

        return personaRepository.findByUsuario(usuario);
    }

    @Override
    public Usuario obtenerUsuarioPorUsername(String username){
        return usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsuarioNoencontradoException("No se encontro el usuario el username: " + username));
    }

    @Transactional
    @Override
    public Usuario registrarUsuario(String nombre, String apellido, String password, Persona persona){

        String username = usuarioUtils.generarUsername(nombre, apellido);
        validarContraseñaUsuario(password);
        
        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setPersona(persona);
        usuario.setRol(TipoEnum.ESTANDAR);

        //SECURITY
        usuario.setAccountNoExpired(true);
        usuario.setAccountNoLocked(false);
        usuario.setCredentialNoExpired(true);
        usuario.setEnabled(true);

        Roles rol = rolesService.buscarRoles(RoleEnum.CLIENTE);
        usuario.setRoles(Set.of(rol));

        return usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public Usuario adminRegistrarUsuario(String username, String password, Persona persona, String rol, String permisos){

        Usuario usuario = new Usuario();
        
        validarNombreUsuario(username, 0);

        usuario.setUsername(username);

        validarContraseñaUsuario(password);
        
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setPersona(persona);
        usuario.setRol(TipoEnum.valueOf(rol));
        

        usuario.setAccountNoExpired(true);
        usuario.setAccountNoLocked(true);
        usuario.setCredentialNoExpired(true);
        usuario.setEnabled(true);

        Roles roles = rolesService.buscarRoles(RoleEnum.valueOf(permisos));
        usuario.setRoles(Set.of(roles));

        return usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public void BloqueoUserFailureAuthentication(String username) {
        Usuario userFailer = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsuarioNoencontradoException("El usuario no se encontro"));

        userFailer.setAccountNoLocked(false);
        logger.warn("Usuario {} bloqueado por multiples fallos de autenticacion", username);

        usuarioRepository.save(userFailer);
       /*  Timer timer = new Timer();
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                DesbloqueoUserFailureAuthentication(username);
                logger.warn("Usuario {} desbloqueado despues de tiempo de espera", username);
            }   
        };
        timer.schedule(tarea, 900000); */
    }

    @Transactional
    @Override
    public void DesbloqueoUserFailureAuthentication(String username){
        Usuario userTrue = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsuarioNoencontradoException("El usuario no se encontro"));

        userTrue.setAccountNoLocked(true);
        logger.warn("Usuario {} desbloqueado despues de tiempo de espera", username);
        
        usuarioRepository.save(userTrue);
    }

    @Override
    public void validarNombreUsuario(String username, int idActual){

        Optional<Usuario> existente = usuarioRepository.findByUsername(username);

        if (existente.isPresent() && !existente.get().getIdUsuario().equals(Integer.valueOf(idActual))) {
            throw new IllegalArgumentException("El usuario ya existe, cambiar username " + username);
        }
    }

    public void validarContraseñaUsuario(String password){
        if (!usuarioUtils.validarPassword(password)) {
            throw new PasswordInvalidaException("La contraseña debe tener exactamente cuatro digitos.");
        }
    }
}
