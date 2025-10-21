package com.banco.sistemabancario.security.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.banco.sistemabancario.entity.Usuario;
import com.banco.sistemabancario.security.jwt.JwtUtils;
import com.banco.sistemabancario.serviceImpl.UsuarioServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter{

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, 
                                    @NotNull HttpServletResponse response, 
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        
        String tokenheader = request.getHeader("Authorization");

        if(tokenheader != null && tokenheader.startsWith("Bearer ")){
            
            String token = tokenheader.substring(7);

            if (jwtUtils.isTokenValid(token)) {
                String username = jwtUtils.getUsernameFromToken(token);
                UserDetails userDetails = usuarioService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = 
                            new UsernamePasswordAuthenticationToken(username, null ,userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } 
        }

        filterChain.doFilter(request, response);
    }
    
}
 