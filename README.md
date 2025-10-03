##  Sistema de Pedidos — Plataforma de Gestión Extensible

**Spring Boot · JPA · Vue.js · JWT · Docker · Arquitectura en Capas · Patrón Fábrica**

Diseñé y desarrollé, en equipo, un sistema completo de gestión de pedidos que permite a los usuarios registrarse, realizar compras con atributos dinámicos, aplicar reglas de negocio configurables y administrar el ciclo de vida de los pedidos.

### 🧠 Backend
- Desarrollado con **Spring Boot**, implementando una **arquitectura en capas** (Controller–Service–Repository).
- Modelo de datos flexible que soporta reglas de negocio extensibles mediante una jerarquía de clases `OrderRule` y fábricas de reglas (`And`, `Or`, `Not`).
- **Persistencia con JPA** sobre base de datos relacional, asegurando integridad y consistencia de datos en entidades como `Order`, `Product`, `User` y `OrderProduct`.
- **Autenticación segura** con Basic Auth y JWT.
- **Notificaciones por correo electrónico**, validaciones dinámicas y panel de administración para gestionar stock y estados de pedidos.

### 🌐 Frontend
- Construido con **Vue.js**, utilizando componentes reactivos y **Axios** para consumir APIs.
- Interfaz funcional para usuarios y administradores.

### 🧱 Arquitectura y Herramientas
- La arquitectura permite **extender reglas de negocio sin modificar el código base**, favoreciendo mantenibilidad y escalabilidad.
- **Docker** para aislar entornos de desarrollo.
- **Postman** para pruebas automatizadas de los endpoints.

## 🐳 Run using Docker Compose

Para levantar el proyecto utilizando **Docker Compose**, seguí estos pasos:

1. Crear un archivo `.env` en la raíz del proyecto con la siguiente estructura:

    ```env
    POSTGRES_USERNAME={username}
    POSTGRES_PASSWORD={password}
    POSTGRES_DOCKER_URL=jdbc:postgresql://database:{port}/{database}
    POSTGRES_LOCAL_URL=jdbc:postgresql://localhost:{port}/{database}
    POSTGRES_DB={database}
    JWT_SECRET={jwt_secret}
    EMAIL_PORT={port}
    EMAIL_USERNAME={email}
    EMAIL_PASSWORD={app_password}
    ```

    **Nota:**  
    - Valores por defecto → `username=postgres`, `port=5432`.

2. Ejecutar el siguiente comando:

    ```bash
    docker-compose up --build
    ```

Esto construirá las imágenes y levantará todos los servicios definidos en el `docker-compose.yml`.

