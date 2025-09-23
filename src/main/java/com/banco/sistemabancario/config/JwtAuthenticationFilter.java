package com.banco.sistemabancario.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.banco.sistemabancario.entity.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private JwtUtils JwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.JwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, 
                                                     HttpServletResponse response)throws AuthenticationException {
        
        Usuario usuario = null;
        String username = "";
        String password = "";
        
        try{
            usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

            username = usuario.getUsername();
            password = usuario.getPassword();

        }catch(Exception e){
            e.printStackTrace() ;

        }

        UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(username, password); //AUTENTICAR CON LOS DOS PARAMETROS
        return getAuthenticationManager().authenticate(authenticationToken);
    }


    //EN CASO DE EXITO:
    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                            HttpServletResponse response, 
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        
        User user = (User) authResult.getPrincipal();           //OBTENER EL DETALLES DEL USUARIO AUTENTICADO  
        
        String token = JwtUtils.generateAccessToken(user.getUsername()); //GENERAR EL TOKEN

        response.addHeader("Authorization", token);
        
        //RESPONDER
        Map<String, Object> httpresponse = new HashMap<>();
        httpresponse.put("token", token);
        httpresponse.put("Message", "Autenticacion exitosa");
        httpresponse.put("username", user.getUsername());

        response.getWriter().write(new ObjectMapper().writeValueAsString(httpresponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush(); //FORZAR

        super.successfulAuthentication(request, response, chain, authResult);
    }

    
}
