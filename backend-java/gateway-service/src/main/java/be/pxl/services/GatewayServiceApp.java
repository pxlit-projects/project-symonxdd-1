package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApp {

  private static final Logger logger = LoggerFactory.getLogger(GatewayServiceApp.class);

  public static void main(String[] args) {
    logger.info("Starting GatewayServiceApp...");
    SpringApplication.run(GatewayServiceApp.class, args);
  }
}
