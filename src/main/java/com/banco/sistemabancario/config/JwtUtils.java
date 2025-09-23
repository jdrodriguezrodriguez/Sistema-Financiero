package com.banco.sistemabancario.config;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.banco.sistemabancario.exception.GlobalExceptionHandler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final Logger logger =  LoggerFactory.getLogger(JwtUtils.class);
    private final GlobalExceptionHandler globalExceptionHandler;
    
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    JwtUtils(GlobalExceptionHandler globalExceptionHandler) {
        this.globalExceptionHandler = globalExceptionHandler;
    }

    //CREAER TOKEN
    public String generateAccessToken(String username){
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
            .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    //VALIDAR TOKEN DE ACCESO
    public boolean isTokenValid(String token){
        try {
            Jwts.parser()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

            return true;
        } catch (Exception e) {
            logger.error("Error al validar el token", e);
            return false;
        }
    }

    //OBTENER EL USERNAME DEL TOKEN+
    public String getUsername(String token){
        return getClaim(token, Claims::getSubject);
    }

    //OBTENER UN SOLO CLAIM
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction){
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    //OBTENER TODOS LOS CLAIMS DEL TOKEN
    public Claims extractAllClaims(String token){
        return Jwts.parser()
            .setSigningKey(getSignatureKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    //OBTENER FIRMA DEL TOKEN
    public Key getSignatureKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);    //DECODIFICAR LA CLAVE SECRETA
        return Keys.hmacShaKeyFor(keyBytes);                    //CONVERTIR A OBJETO
    }
}
