package be.pxl.services.config;

import org.springframework.context.annotation.Bean; // Import the Bean annotation to define beans in Spring context
import org.springframework.context.annotation.Configuration; // Import the Configuration annotation for defining configuration classes
import org.springframework.web.cors.CorsConfiguration; // Import CorsConfiguration to configure CORS settings
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource; // Import UrlBasedCorsConfigurationSource for configuring CORS in a reactive manner

import be.pxl.services.GatewayServiceApp;

import org.springframework.web.cors.reactive.CorsWebFilter; // Import CorsWebFilter to apply CORS configuration globally

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays; // Import Arrays utility for convenient array operations

@Configuration // Indicates that this class contains Spring configuration
public class GatewayConfig {

  private static final Logger logger = LoggerFactory.getLogger(GatewayServiceApp.class);

  @Bean // Declares this method as a Spring bean
  public CorsWebFilter corsWebFilter() {
    logger.info("Configuring CORS");

    CorsConfiguration configuration = new CorsConfiguration(); // Create a new CORS configuration

    // Allow requests from this origin
    // 4200 is for Angular dev server; 8080 is for Docker nginx server

    logger.info("Allowing requests from http://localhost:4200 and http://localhost:8080");
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8080"));

    logger.info("Allowing all HTTP methods and headers");
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow these HTTP verbs

    logger.info("Allowing all headers");
    configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Create a new CORS configuration
                                                                                    // source

    logger.info("Registering CORS configuration for all paths");
    source.registerCorsConfiguration("/**", configuration); // Register the CORS configuration for all paths

    logger.info("Returning CorsWebFilter with registered CORS configuration");
    return new CorsWebFilter(source); // Return a new CorsWebFilter with the registered CORS configuration
  }
}
