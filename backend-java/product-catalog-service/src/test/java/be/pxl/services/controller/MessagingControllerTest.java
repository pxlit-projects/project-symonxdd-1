package be.pxl.services.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(MessagingController.class)
public class MessagingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RabbitTemplate rabbitTemplate;

  @Test
  public void sendMessageTest() throws Exception {
    String message = "Hello, World!";

    // Perform the POST request to /messaging/send with the message parameter
    mockMvc.perform(post("/messaging/send")
        .param("message", message)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk())
        .andExpect(content().string("Message sent to the queue!"));

    // Verify that RabbitTemplate's convertAndSend method was called with the
    // correct arguments
    Mockito.verify(rabbitTemplate).convertAndSend("myQueue", message);
  }
}
