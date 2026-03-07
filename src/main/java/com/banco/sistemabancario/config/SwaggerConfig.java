package com.banco.sistemabancario.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Sistema Financiero API",
        version = "1.0",
        description = "API REST para la gestión de cuentas, clientes y transacciones"
    )
)
public class SwaggerConfig {
    
}
