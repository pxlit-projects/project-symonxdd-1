package be.pxl.services.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messaging")
@RequiredArgsConstructor
public class MessagingController {

  // Inject RabbitTemplate
  private final RabbitTemplate rabbitTemplate;

  @PostMapping("/send")
  public ResponseEntity<String> sendMessage(@RequestParam String message) {

    // Sends message to the queue named "myQueue"
    rabbitTemplate.convertAndSend("myQueue", message);

    return new ResponseEntity<>("Message sent to the queue!", HttpStatus.OK);
  }
}