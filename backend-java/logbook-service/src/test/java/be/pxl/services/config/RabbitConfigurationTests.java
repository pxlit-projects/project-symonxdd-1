package be.pxl.services.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.RabbitMQContainer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RabbitConfigurationTests {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private Jackson2JsonMessageConverter jsonMessageConverter;

  @Autowired
  private Queue queue;

  private static RabbitMQContainer rabbitMQContainer;

  @BeforeAll
  public static void setUp() {
    rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.8-management")
        .withExposedPorts(5672, 15672);
    rabbitMQContainer.start();
  }

  @Test
  public void testRabbitTemplateBean() {
    assertNotNull(rabbitTemplate, "RabbitTemplate bean should be created");
    assertNotNull(rabbitTemplate.getMessageConverter(), "RabbitTemplate should have a message converter set");
  }

  @Test
  public void testJackson2JsonMessageConverterBean() {
    assertNotNull(jsonMessageConverter, "Jackson2JsonMessageConverter bean should be created");
  }

  @Test
  public void testQueueBean() {
    assertNotNull(queue, "Queue bean should be created");
    assertEquals("myQueue", queue.getName(), "Queue name should be 'myQueue'");
  }
}