package be.pxl.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApp {

  private static final Logger logger = LoggerFactory.getLogger(DiscoveryServiceApp.class);

  public static void main(String[] args) {
    logger.info("Starting DiscoveryServiceApp...");
    SpringApplication.run(DiscoveryServiceApp.class, args);
  }
}
