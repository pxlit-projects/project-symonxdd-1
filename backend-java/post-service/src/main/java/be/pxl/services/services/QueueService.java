package be.pxl.services.services;

// import be.pxl.services.domain.dto.ProductChangeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

  private static final Logger logger = LoggerFactory.getLogger(QueueService.class);

  @RabbitListener(queues = "myQueue")
  public void listen() {

    StringBuilder messageBuilder = new StringBuilder();

    logger.info(messageBuilder.toString());
  }
}
