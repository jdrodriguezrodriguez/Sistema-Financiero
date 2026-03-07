# Sistema Financiero - API Backend

API backend desarrollada con Spring Boot que simula un sistema financiero con gestión de usuarios, cuentas y transacciones.
El proyecto fue construido con fines de aprendizaje y práctica en desarrollo backend, aplicando buenas prácticas de arquitectura, seguridad y manejo de eventos.

# Tecnologías utilizadas

* Java
* Spring Boot
* Spring Security
* Spring Data JPA / Hibernate
* JWT Authentication
* Swagger
* Maven
* MySQL
* JUnit / Mockito


# Funcionalidades principales

* Registro y autenticación de usuarios
* Gestión de cuentas bancarias
* Transferencias entre cuentas
* Depósitos y retiros
* Auditoría de operaciones
* Eventos asíncronos (ej: envío de correo en transacciones)
* Manejo global de excepciones
* Seguridad con password encryption
* Control de intentos fallidos de login


# Conceptos aplicados

Durante el desarrollo se aplicaron conceptos importantes de backend:

* Arquitectura en capas
* DTO Pattern
* Manejo de eventos en Spring
* Seguridad con Spring Security
* Validación de datos
* Manejo global de errores
* Testing de servicios y controladores


# Estructura del proyecto

```
src/main/java/com/banco/sistemabancario

config        → configuraciones de Spring
controller    → endpoints de la API
dto           → objetos de transferencia de datos
entity        → entidades JPA
events        → definición de eventos del sistema
listener      → listeners de eventos asíncronos
exception     → manejo global de excepciones
repository    → acceso a base de datos
security      → configuración de seguridad
service       → interfaces de servicios
serviceImpl   → implementación de lógica de negocio
util          → utilidades
```


# Documentación de la API

La documentación está disponible con **Swagger**



# Objetivo del proyecto

Este proyecto fue desarrollado como práctica para mejorar habilidades en desarrollo backend con Spring Boot, enfocándose en:

* Diseño de APIs REST
* Seguridad
* Arquitectura backend
* Manejo de eventos
* Testing


# Autor

**Juan Rodriguez**

https://github.com/jdrodriguezrodriguez
