package com.adrian.gameconcepthub.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Beans para la aplicación.
 * 
 * La mayoría de los repositorios y controladores ahora se registran automáticamente
 * mediante anotaciones @Repository, @Service, @RestController y @Component de Spring.
 * 
 * Esta clase se mantiene como configuración centralizada para futuras necesidades.
 */
@Configuration
public class BeanConfig {
    // Los repositorios Spring Data JPA se registran automáticamente
    // Los servicios se registran con @Service
    // Los controladores se registran con @RestController
    // La inyección de dependencias ocurre automáticamente
}
