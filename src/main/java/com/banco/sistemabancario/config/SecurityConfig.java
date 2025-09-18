package com.banco.sistemabancario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;





@Configuration
@EnableWebSecurity
@EnableMethodSecurity                       //PERMITE TRABAJAR CON ANOTACIONES
public class SecurityConfig{

    //FILTRO (CONDICIONES PERSONALIZADAS)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        
        return httpSecurity
            .csrf(csrf -> csrf.disable()) //VULNERABILIDAD EN LOS FORM WEB
            .authorizeHttpRequests(auth -> {

                //CONFIGURAR LOS ENDPOINTS PUBLICOS
                auth.requestMatchers(HttpMethod.GET, "/", "/login.html", "/register.html", "/css/**", "/js/**").permitAll();   
                auth.requestMatchers(HttpMethod.POST, "/autenticar").permitAll();                 
                auth.requestMatchers(HttpMethod.POST, "/registro").permitAll();


                //CONFIGURAR LOS ENDPOINTS PRIVADOS
                auth.requestMatchers(HttpMethod.GET, "/index.html", "/index").hasAuthority("CREATE");
                auth.requestMatchers(HttpMethod.GET, "/admin.html", "/admin").hasRole("ADMIN");    

                //CONFIGURAR LOS ENDPOINTS NO ESPECIFICADOS
                auth.anyRequest().authenticated();                                     
            })
            .sessionManagement(session -> //ADMINISTRADOR DE LA SESION
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //NO GUARDA LA SESSION EN MEMORIA
            .httpBasic()
            .and()
            .build();
    }

    //GESTIONA EL PROCESO DE AUTENTICACION
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    //PROVEEDOR - BUSCA LOS USUARIOS Y DEMAS EN BASE DE DATOS POR MEDIO DEL SERVICIO EN USUARIOS 
    //(CONVIERTE LOS DATOS DEL USUARIO COMO ROLES, PERMISOS Y DEMAS EN UN OBJETO DE S.SECURITY)
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
