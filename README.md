# Game Concept Hub

## Descripcion

Game Concept Hub es un proyecto backend pensado para organizar, documentar y consultar conceptos de videojuegos. La idea general es tener un espacio estructurado donde cada juego o idea de juego pueda almacenarse con su informacion principal, categorias, etiquetas y secciones tematicas como historia, mundo, gameplay o concepto.

El proyecto esta planteado con una arquitectura inspirada en Hexagonal Architecture o Clean Architecture. Eso permite separar la logica del dominio de los detalles de persistencia y de la capa web, haciendo mas sencillo evolucionar el sistema a futuro.

## Para que sirve

Este proyecto puede usarse como base para:

- Registrar ideas de videojuegos.
- Consultar fichas de conceptos ya creados.
- Buscar conceptos por nombre.
- Clasificar juegos con categorias y tags.
- Preparar una futura interfaz web o panel administrativo.

## Idea general del sistema

La entidad principal es `Game`, que representa un concepto de videojuego. Cada juego puede incluir:

- `name`: nombre del juego o concepto.
- `description`: descripcion general.
- `genre`: genero principal.
- `categories`: clasificacion tematica o jerarquica.
- `tags`: palabras clave para busqueda o filtrado.
- `sections`: bloques de contenido mas detallados, por ejemplo historia, mundo, gameplay o concepto.

Ademas, el proyecto contempla una base para autenticacion mediante `User`, lo que abre el camino a una administracion interna del contenido.

## Estructura del proyecto

```text
main.java.com.adrian.gameconcepthub
|
|-- domain                      (nucleo del negocio)
|   |-- model
|   |   |-- Game.java
|   |   |-- Category.java
|   |   |-- Tag.java
|   |   |-- Section.java
|   |   |-- SectionType.java
|   |   |-- User.java
|   |
|   |-- port
|       |-- in                 (casos de uso)
|       |   |-- CreateGameUseCase.java
|       |   |-- GetGameUseCase.java
|       |   |-- SearchGameUseCase.java
|       |
|       |-- out                (dependencias del dominio)
|           |-- GameRepository.java
|           |-- CategoryRepository.java
|           |-- TagRepository.java
|           |-- UserRepository.java
|
|-- application                (implementacion de casos de uso)
|   |-- service
|       |-- GameService.java
|
|-- infrastructure             (adaptadores externos)
|   |-- persistence
|   |   |-- entity
|   |   |   |-- GameEntity.java
|   |   |   |-- CategoryEntity.java
|   |   |   |-- TagEntity.java
|   |   |   |-- SectionEntity.java
|   |   |   |-- UserEntity.java
|   |   |
|   |   |-- repository
|   |       |-- JpaGameRepository.java
|   |       |-- JpaCategoryRepository.java
|   |       |-- JpaTagRepository.java
|   |       |-- JpaUserRepository.java
|   |
|   |-- web
|       |-- controller
|           |-- GameController.java
|           |-- AuthController.java
|
|-- config
|   |-- BeanConfig.java
|
|-- GameConceptHubApplication.java
```

## Tecnologias y version de Java

- Lenguaje: Java
- Build tool: Maven
- Version configurada del proyecto: Java 17

Nota: aunque el `pom.xml` esta configurado con Java 17, el codigo fue ajustado para evitar varios metodos modernos que suelen dar problemas en entornos mas viejos, como `.toList()` o `.isBlank()`.

## Estado actual del proyecto

Actualmente el proyecto funciona como una base backend estructurada y coherente, con implementaciones internas para el flujo principal de juegos, categorias, etiquetas y usuarios. La persistencia usada en este momento es una implementacion simple en memoria, util para desarrollo inicial y pruebas de estructura.

Esto significa que el proyecto ya sirve para validar el modelo, las relaciones entre capas y la organizacion del sistema, aunque todavia puede evolucionar hacia una integracion real con Spring Boot, JPA y base de datos.

## Uso actual

El proyecto esta pensado como base para seguir creciendo. En su estado actual permite:

- Crear juegos desde la capa de aplicacion.
- Obtener un juego por id.
- Buscar juegos por nombre.
- Registrar y consultar usuarios basicos.

La clase principal es `GameConceptHubApplication`, y la configuracion manual de dependencias esta centralizada en `BeanConfig`.

## Proximos pasos recomendados

- Agregar pruebas unitarias y de integracion.
- Incorporar persistencia real con Spring Data JPA.
- Exponer endpoints HTTP reales con Spring MVC o Spring Boot.
- Añadir validaciones mas robustas y seguridad.
- Crear una interfaz para administracion de conceptos.
