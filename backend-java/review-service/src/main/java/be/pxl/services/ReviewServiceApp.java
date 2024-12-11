package be.pxl.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ReviewServiceApp {

  private static final Logger logger = LoggerFactory.getLogger(ReviewServiceApp.class);

  public static void main(String[] args) {
    logger.info("Starting ReviewServiceApp...");
    SpringApplication.run(ReviewServiceApp.class, args);
  }
}
