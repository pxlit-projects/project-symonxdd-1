// package be.pxl.services.services;

// import be.pxl.services.domain.dto.ProductChangeDTO;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.amqp.core.Queue;
// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.amqp.rabbit.core.RabbitAdmin;
// import org.springframework.amqp.rabbit.connection.ConnectionFactory;
// import org.springframework.amqp.rabbit.connection.ConnectionFactoryUtils;
// import org.testcontainers.containers.RabbitMQContainer;

// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.assertTrue;

// @ExtendWith(SpringExtension.class)
// @SpringBootTest
// public class QueueServiceTests {

// private static RabbitMQContainer rabbitMQContainer;
// private static TestAppender testAppender;

// @Autowired
// private RabbitTemplate rabbitTemplate;

// @Autowired
// private Queue queue;

// @Autowired
// private QueueService queueService; // This is to invoke the listener method
// manually if needed

// @BeforeAll
// public static void setUp() {
// rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.8-management")
// .withExposedPorts(5672, 15672);
// rabbitMQContainer.start();

// // Set up logging
// testAppender = new TestAppender();
// Logger logger = LoggerFactory.getLogger(QueueService.class);
// ((ch.qos.logback.classic.Logger) logger).addAppender(testAppender);
// }

// @Test
// public void testListen_WithValidMessage() {
// // Arrange
// ProductChangeDTO changeDTO = new ProductChangeDTO();
// changeDTO.setProductId(1L);
// changeDTO.setRole("admin");
// changeDTO.setOldName("OldName");
// changeDTO.setNewName("NewName");

// // Act
// rabbitTemplate.convertAndSend("myQueue", changeDTO);

// // Wait for the message to be processed
// try {
// Thread.sleep(1000); // Sleep to allow async processing
// } catch (InterruptedException e) {
// e.printStackTrace();
// }

// // Assert
// String logMessage = testAppender.getLogMessages().get(0);
// assertTrue(logMessage.contains("Product ID: 1, Rol: admin"));
// assertTrue(logMessage.contains("Naam van 'OldName' naar 'NewName'"));
// }

// private static class TestAppender extends
// ch.qos.logback.core.AppenderBase<ch.qos.logback.classic.spi.ILoggingEvent> {

// private final List<String> logMessages = new ArrayList<>();

// @Override
// protected void append(ch.qos.logback.classic.spi.ILoggingEvent eventObject) {
// logMessages.add(eventObject.getFormattedMessage());
// }

// public List<String> getLogMessages() {
// return logMessages;
// }
// }

// @Configuration
// static class TestConfig {
// @Bean
// public Queue myQueue() {
// return new Queue("myQueue", false);
// }
// }
// }