package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LogbookServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LogbookServiceApplication.class, args);
  }
}
