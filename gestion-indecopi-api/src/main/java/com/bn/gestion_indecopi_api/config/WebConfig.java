package com.bn.gestion_indecopi_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Cambiado de /api/** a /** para cubrir TODO el proyecto
                .allowedOrigins("*") // LIBRE: Permite cualquier origen (Vercel, ngrok, etc.)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Todos los verbos necesarios
                .allowedHeaders("*") // Permite cualquier cabecera (incluyendo el skip-warning de ngrok)
                .allowCredentials(false); // OBLIGATORIO: Debe ser false si usas "*" en allowedOrigins
    }
}