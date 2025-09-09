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
import org.springframework.security.config.core.userdetails.UserDetailsResourceFactoryBean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;



import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

    /*
    private AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration){
        this.authenticationConfiguration = authenticationConfiguration;
    }*/
    

    //FILTRO (CONDICIONES PERSONALIZADAS)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        /*httpSecurity.authorizeHttpRequests(auth -> auth
            .requestMatchers("/login/**")
            .permitAll()
            .anyRequest().authenticated()
            )
            .formLogin(withDefaults())
            .logout(withDefaults());

        return httpSecurity.build();*/

        return httpSecurity
            .csrf(csrf -> csrf.disable()) //VULNERABILIDAD EN LOS FORM WEB
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //NO GUARDA LA SESSION EN MEMORIA
            .httpBasic(Customizer.withDefaults())                                       //SE USA PARA USU Y PASS - CON TOKEN ES DISTINTO
            .authorizeHttpRequests(http -> {
                http.requestMatchers(HttpMethod.GET, "/").permitAll();                  //ENDPOINT PERMITIDO PARA TODOS
                http.requestMatchers(HttpMethod.POST, "/login").permitAll();                  //ENDPOINT PERMITIDO PARA TODOS
                http.requestMatchers(HttpMethod.GET, "/index").hasAuthority("READ");    //ENDPOINT DEBE TENER LA AUTORIZACION DADA EN EL USERDETAILSSERVICE

                http.anyRequest().denyAll();                                            //EL RESTO, SERA DENEGADO
            }).build();
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
    public UserDetailsService userDetailsService(){
        UserDetails userDetails = User.withUsername("juan")
            .password("root")
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
