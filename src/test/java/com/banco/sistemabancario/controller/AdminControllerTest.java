package com.banco.sistemabancario.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.banco.sistemabancario.dto.admin.ConsultarUsuarioAdmin;
import com.banco.sistemabancario.security.jwt.JwtUtils;
import com.banco.sistemabancario.serviceImpl.admin.AdminServiceImpl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* @WebMvcTest(AdminController.class)
@Import(SecurityConfig.class) */
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @MockBean
    private AdminServiceImpl adminServiceImpl;

    @Test
    public void denegarAccesoConsultarDatosUsuario() throws Exception{
        mockMvc.perform(get("/api/sistema/admin/usuario/datos")
                .param("documento", "1110450635"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void denegarAccesoSinAutorizacionDatosUsuarios() throws Exception{
        mockMvc.perform(get("/api/sistema/admin/usuario/datos")
                .param("documento", "1110450635"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void accesoAdminDatosUsuario() throws Exception{

        String token = jwtUtils.generateAccessToken("root");

        ConsultarUsuarioAdmin datos = new ConsultarUsuarioAdmin();
        datos.setUsername("root");

        when(adminServiceImpl.adminBuscarUsuario("1110450635")).thenReturn(datos);

        mockMvc.perform(get("/api/sistema/admin/usuario/datos")
                .param("documento", "1110450635")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(datos.getUsername()));       
    }
}
