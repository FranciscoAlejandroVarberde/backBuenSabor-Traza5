package com.entidades.buenSabor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class corsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Permitir credenciales (por ejemplo, cookies)
        // Configurar los orígenes permitidos o patrones de origen permitidos
        // Por ejemplo, permitir solicitudes desde http://localhost:5173 y http://localhost:8080
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://localhost:5174");
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("https://localhost:5173");
        config.addAllowedOrigin("https://localhost:5174");
        config.addAllowedOrigin("https://dashboard-front-five.vercel.app");
        config.addAllowedOrigin("https://buensaborbackend.onrender.com");
        config.addAllowedOrigin("https://localhost:8080");
        config.addAllowedHeader("*"); // Permitir todos los encabezados
        config.addAllowedMethod("*"); // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE, etc.)
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}