package be.pxl.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PostServiceApp {

  private static final Logger logger = LoggerFactory.getLogger(PostServiceApp.class);

  public static void main(String[] args) {
    logger.info("Starting PostServiceApp...");
    SpringApplication.run(PostServiceApp.class, args);
  }
}
