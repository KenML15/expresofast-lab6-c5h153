# Laboratorio 6 — ExpresoFast (Parte II)

**Curso:** IF0009 – Desarrollo de Software IV
**Ciclo:** II-2026
**Universidad de Costa Rica — Sede del Atlántico, Recinto Paraíso**
**Estudiante:** [Su nombre completo]
**Carné:** [Su carné, ej. C5H153]

Plataforma full-stack de logística ExpresoFast, Parte II: seguridad con JWT,
control de acceso basado en roles (RBAC), DTOs con validación estricta,
manejo global de excepciones y bitácora de auditoría de envíos.

## Requisitos de entorno

- Java 21
- Maven 3.9+
- Microsoft SQL Server (Developer Edition) + SSMS
- Navegador web moderno (Chrome/Edge/Firefox) con DevTools

## Configuración de la base de datos

1. Ejecute en SSMS, en orden:
   - `database/01_schema_lab5.sql` (esquema base del Laboratorio 5)
   - `database/02_schema_lab6_extension.sql` (tablas Usuario, Rol, UsuarioRol, BitacoraEnvio)
   - `database/03_data_seeds.sql` (roles y usuarios de prueba con contraseñas ya encriptadas en BCrypt)
2. Copie `backend/application.properties.template` a
   `backend/expresofast/src/main/resources/application.properties` y complete
   su usuario/contraseña de SQL Server y una clave JWT propia
   (`app.jwt.secret`). Este archivo **no se sube a GitHub** (ver `.gitignore`).

## Usuarios de prueba

Contraseña para todos: `Password123!`

| Usuario      | Contraseña     | Rol             |
|--------------|----------------|-----------------|
| admin        | Password123!   | ROLE_ADMIN      |
| operador1    | Password123!   | ROLE_OPERADOR   |
| conductor1   | Password123!   | ROLE_CONDUCTOR  |

## Ejecución

**Backend:**
```bash
cd backend/expresofast
mvn spring-boot:run
```
El API queda disponible en `http://localhost:8080`.

**Frontend:**
Abra `frontend/login.html` con Live Server (VS Code) o cualquier servidor
estático (ej. `http://127.0.0.1:5500/frontend/login.html`). Inicie sesión con
cualquiera de los usuarios de prueba.

## Matriz de permisos (RBAC)

| Endpoint | Método | Roles permitidos |
|---|---|---|
| `/api/auth/login` | POST | Público |
| `/api/envios/optimizados` | GET | ADMIN, OPERADOR, CONDUCTOR |
| `/api/envios` | POST | ADMIN, OPERADOR |
| `/api/envios/{id}/estado` | PATCH | ADMIN, CONDUCTOR |
| `/api/envios/{id}/bitacora` | GET | ADMIN, OPERADOR |
| `/api/vehiculos/**` | ALL | ADMIN |

## Estructura del repositorio

```
expresofast-lab6-carnet/
├── backend/
│   ├── expresofast/ (proyecto Maven Spring Boot)
│   └── application.properties.template
├── database/
│   ├── 01_schema_lab5.sql
│   ├── 02_schema_lab6_extension.sql
│   └── 03_data_seeds.sql
├── frontend/
│   ├── index.html
│   ├── login.html
│   ├── styles.css
│   └── app.js
├── docs/
│   └── ExpresoFast_Postman_Collection.json
└── README.md
```
