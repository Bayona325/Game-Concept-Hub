# Game Concept Hub - Guía de Sistema de Autenticación y Base de Datos

## 📊 Cambios Principales Realizados

### 1. Base de Datos Persistente ✅
**Antes**: H2 en memoria (datos se perdían al reiniciar)
**Ahora**: H2 persistente en archivo local

```
./data/gamedb.mv.db  ← Base de datos almacenada aquí
```

El proyecto ahora **guarda todos los datos** incluso después de apagar y encender la aplicación.

### 2. Sistema de Autenticación Implementado ✅
La aplicación ahora tiene:
- **Registro de usuarios** - Crear nuevas cuentas
- **Login** - Autenticarse con usuario/contraseña
- **Sesiones** - Mantener usuario logueado
- **Privacidad de datos** - Cada usuario solo ve sus juegos

---

## 🚀 Cómo Ejecutar la Aplicación

### 1. Compilar (si es necesario)
```bash
cd /home/adrian/Game_Concept_Hub/Game-Concept-Hub
mvn clean package
```

### 2. Ejecutar la aplicación
```bash
java -jar target/game-concept-hub-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en:
```
http://localhost:8080
```

---

## 🔐 Endpoints de Autenticación

### Registro de Usuario
```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "nuevo_usuario",
  "email": "usuario@example.com",
  "password": "mi_contraseña",
  "passwordConfirm": "mi_contraseña"
}
```

**Respuesta exitosa** (201):
```json
{
  "id": 1,
  "username": "nuevo_usuario",
  "email": "usuario@example.com",
  "message": "Registro exitoso. Por favor, inicia sesión.",
  "success": true
}
```

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "nuevo_usuario",
  "password": "mi_contraseña"
}
```

**Respuesta exitosa** (200):
```json
{
  "id": 1,
  "username": "nuevo_usuario",
  "email": "usuario@example.com",
  "message": "Inicio de sesión exitoso",
  "success": true
}
```

### Verificar Usuario Actual
```bash
GET /api/auth/me
```

### Logout
```bash
POST /api/auth/logout
```

### Verificar si está autenticado
```bash
GET /api/auth/check
```

---

## 📚 Endpoints de Juegos (Ahora requieren autenticación)

### Obtener todos los juegos del usuario
```bash
GET /api/games
```

### Crear un juego
```bash
POST /api/games
Content-Type: application/json

{
  "name": "Mi Nuevo Juego",
  "description": "Descripción del juego",
  "genre": "Acción",
  "categories": [
    {"name": "2D"},
    {"name": "Indie"}
  ],
  "tags": [
    {"name": "Retro"},
    {"name": "Nostálgico"}
  ],
  "sections": []
}
```

### Buscar juegos
```bash
GET /api/games/search?query=acción
```

### Obtener un juego específico
```bash
GET /api/games/{id}
```

### Actualizar un juego
```bash
PUT /api/games/{id}
Content-Type: application/json

{
  "name": "Nombre actualizado",
  "description": "Nueva descripción",
  "genre": "Aventura",
  "sections": [...]
}
```

### Eliminar un juego
```bash
DELETE /api/games/{id}
```

---

## 📁 Archivos Principales Modificados

| Archivo | Cambio |
|---------|--------|
| `pom.xml` | Agregar Spring Security + PostgreSQL |
| `application.properties` | Base de datos persistente + logging |
| `UserEntity.java` | Anotaciones JPA + relación a juegos |
| `GameEntity.java` | Relación @ManyToOne con usuario |
| `AuthService.java` | NUEVO - Lógica de autenticación |
| `AuthController.java` | ACTUALIZADO - Nuevos endpoints |
| `GameController.java` | ACTUALIZADO - Validación de usuario |
| `SecurityConfig.java` | NUEVO - Configuración de seguridad |
| `JpaUserRepository.java` | NUEVO - Repositorio Spring Data JPA |
| `JpaGameRepository.java` | ACTUALIZADO - Métodos con filtro de usuario |

---

## 🔒 Seguridad Implementada

### Encriptación de Contraseñas
- Las contraseñas se guardan **encriptadas** con BCrypt
- Validación segura de contraseñas en login

### Aislamiento de Datos
- Cada usuario **solo ve sus propios juegos**
- No es posible acceder a juegos de otros usuarios

### Validación de Sesión
- El usuario debe estar logueado para acceder a `/api/games`
- Retorna 401 Unauthorized si no hay sesión

---

## 📝 Próximos Pasos

### 1. Actualizar la Interfaz Web
La página web actual necesita ser modificada para:
- Mostrar pantalla de login/registro
- Guardar token de sesión en el navegador
- Mostrar datos de usuario logueado

### 2. Cambiar a PostgreSQL (Opcional)
Para desplegar en la nube, se recomienda PostgreSQL:

```properties
# Cambiar application.properties a:
spring.datasource.url=jdbc:postgresql://localhost:5432/gamedb
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL10Dialect
```

### 3. Agregar JWT (Opcional)
Para mejor escalabilidad, implementar JWT en lugar de sesiones HTTP.

### 4. Desplegar en Railway.app
Ver guía anterior sobre despliegue en la nube.

---

## ✅ Checklist de Funcionalidad

- ✅ Base de datos persistente (datos guardados entre sesiones)
- ✅ Registro de usuarios
- ✅ Login con validación de contraseña
- ✅ Logout y cerrar sesión
- ✅ Cada usuario solo ve sus juegos
- ✅ Contraseñas encriptadas
- ✅ Compilación sin errores
- ⏳ Interfaz web actualizada (Pendiente)

---

## 🐛 Troubleshooting

### "No hay base de datos"
- Asegúrate que el directorio `./data` existe
- Si no existe, se creará automáticamente

### "Usuario no autenticado"
- Primero regístrate en `/api/auth/register`
- Luego inicia sesión en `/api/auth/login`
- Verifica que la sesión esté activa

### "Error de compilación"
```bash
# Limpiar y recompilar
mvn clean compile
mvn package
```

---

**Última actualización**: 20 de Abril de 2026
**Versión**: 0.0.1-SNAPSHOT
