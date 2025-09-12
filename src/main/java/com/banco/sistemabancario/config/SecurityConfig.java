package com.banco.sistemabancario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;





@Configuration
@EnableWebSecurity
@EnableMethodSecurity                       //PERMITE TRABAJAR CON ANOTACIONES
public class SecurityConfig{

    //FILTRO (CONDICIONES PERSONALIZADAS)
    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        
        return httpSecurity
            .csrf(csrf -> csrf.disable()) //VULNERABILIDAD EN LOS FORM WEB
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //NO GUARDA LA SESSION EN MEMORIA
            .formLogin(formLogin -> formLogin.loginPage("/login.html").permitAll())                                     //SE USA PARA USU Y PASS - CON TOKEN ES DISTINTO
            .authorizeHttpRequests(http -> {

                //CONFIGURAR LOS ENDPOINTS PUBLICOS
                http.requestMatchers(HttpMethod.GET, "/").permitAll();   
                http.requestMatchers("/login.html", "/css/**", "/js/**").permitAll();               //ENDPOINT PERMITIDO PARA TODOS
                http.requestMatchers(HttpMethod.POST, "/login").permitAll();                  //ENDPOINT PERMITIDO PARA TODOS

                //CONFIGURAR LOS ENDPOINTS PRIVADOS
                http.requestMatchers(HttpMethod.GET, "/index").hasAuthority("READ");    //ENDPOINT DEBE TENER LA AUTORIZACION DADA EN EL USERDETAILSSERVICE

                //CONFIGURAR LOS ENDPOINTS NO ESPECIFICADOS
                http.anyRequest().denyAll();                                            //EL RESTO, SERA DENEGADO
                //http.anyRequest().authenticated();                                      //ACCEDE SI ESTA AUTENTICADO
            }).build();
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        
        return httpSecurity
            .csrf(csrf -> csrf.disable()) //VULNERABILIDAD EN LOS FORM WEB
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //NO GUARDA LA SESSION EN MEMORIA
            .build();
        }

    //ADMINISTRADOR DE AUTENTICACION
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    //PROVEEDOR
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());

        return provider;
    }

    //DETALLES DEL USUARIO
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails userDetails = User.withUsername("juan")
            .password("{noop}root")
            .roles("ADMIN")
            .authorities("READ", "CREATED")
            .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
   
}
