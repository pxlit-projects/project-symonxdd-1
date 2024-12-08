package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApplication {

  private static final Logger logger = LoggerFactory.getLogger(GatewayServiceApplication.class);

  public static void main(String[] args) {
    logger.info("Starting GatewayServiceApplication");
    SpringApplication.run(GatewayServiceApplication.class, args);
  }
}
