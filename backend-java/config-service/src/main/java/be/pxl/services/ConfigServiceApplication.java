package be.pxl.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServiceApplication {

  private static final Logger logger = LoggerFactory.getLogger(ConfigServiceApplication.class);

  public static void main(String[] args) {
    logger.info("Starting ConfigServiceApplication");
    SpringApplication.run(ConfigServiceApplication.class, args);
  }
}
