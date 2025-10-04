package com.banco.sistemabancario.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.banco.sistemabancario.security.filters.JwtAuthenticationFilter;
import com.banco.sistemabancario.security.filters.JwtAuthorizationFilter;
import com.banco.sistemabancario.security.jwt.JwtUtils;





@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity                       //PERMITE TRABAJAR CON ANOTACIONES
public class SecurityConfig{

    JwtUtils jwtUtils;
    UserDetailsService userDetailsService;
    JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(JwtUtils jwtUtils, UserDetailsService userDetailsService, JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    //FILTRO (CONDICIONES PERSONALIZADAS)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception{
        
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/autenticar"); //CAMBIAR LA RUTA DE LOGIN

        return httpSecurity
            .csrf(csrf -> csrf.disable()) //VULNERABILIDAD EN LOS FORM WEB
            .authorizeHttpRequests(auth -> {

                //CONFIGURAR LOS ENDPOINTS PUBLICOS
                auth.requestMatchers(HttpMethod.GET, "/", "/login.html", "/register.html", "/css/**", "/js/**").permitAll();   
                auth.requestMatchers(HttpMethod.POST, "/autenticar").permitAll();                 
                auth.requestMatchers(HttpMethod.POST, "/registro").permitAll();


                //CONFIGURAR LOS ENDPOINTS PRIVADOS
                auth.requestMatchers(HttpMethod.GET, "/index.html", "/index").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET, "/admin.html", "/admin").hasRole("ADMIN");    

                //CONFIGURAR LOS ENDPOINTS NO ESPECIFICADOS
                auth.anyRequest().authenticated();                                     
            })
            .sessionManagement(session -> //ADMINISTRADOR DE LA SESION
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //NO GUARDA LA SESSION EN MEMORIA
            .addFilter(jwtAuthenticationFilter) //AGREGA EL FILTRO DE AUTENTICACION
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    //GESTIONA EL PROCESO DE AUTENTICACION
    //PROVEEDOR - BUSCA LOS USUARIOS Y DEMAS EN BASE DE DATOS POR MEDIO DEL SERVICIO EN USUARIOS 
    //(CONVIERTE LOS DATOS DEL USUARIO COMO ROLES, PERMISOS Y DEMAS EN UN OBJETO DE S.SECURITY)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, 
                                                    UserDetailsService userDetailsService, 
                                                    PasswordEncoder passwordEncoder) throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
