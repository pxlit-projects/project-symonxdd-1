package be.pxl.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CommentServiceApp {

  private static final Logger logger = LoggerFactory.getLogger(CommentServiceApp.class);

  public static void main(String[] args) {
    logger.info("Starting CommentServiceApp...");
    SpringApplication.run(CommentServiceApp.class, args);
  }
}
