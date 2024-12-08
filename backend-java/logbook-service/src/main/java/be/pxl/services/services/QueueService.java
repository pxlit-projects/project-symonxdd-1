package be.pxl.services.services;

import be.pxl.services.domain.dto.ProductChangeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

  private static final Logger logger = LoggerFactory.getLogger(QueueService.class);

  @RabbitListener(queues = "myQueue")
  public void listen(ProductChangeDTO changeDTO) {

    if (changeDTO == null) {
      logger.warn("Received null message from queue");
      return;
    }

    StringBuilder messageBuilder = new StringBuilder();
    messageBuilder.append("Product ID: ").append(changeDTO.getProductId()).append(", Rol: ")
        .append(changeDTO.getRole());

    if (changeDTO.getOldName() != null || changeDTO.getNewName() != null) {
      messageBuilder.append(", Naam van '").append(changeDTO.getOldName()).append("' naar '")
          .append(changeDTO.getNewName()).append("'");
    }
    if (changeDTO.getOldDescription() != null || changeDTO.getNewDescription() != null) {
      messageBuilder.append(", Beschrijving van '").append(changeDTO.getOldDescription()).append("' naar '")
          .append(changeDTO.getNewDescription()).append("'");
    }
    if (changeDTO.getOldPrice() != null || changeDTO.getNewPrice() != null) {
      messageBuilder.append(", Prijs van '").append(changeDTO.getOldPrice()).append("' naar '")
          .append(changeDTO.getNewPrice()).append("'");
    }
    if (changeDTO.getOldStock() != null || changeDTO.getNewStock() != null) {
      messageBuilder.append(", Voorraad van '").append(changeDTO.getOldStock()).append("' naar '")
          .append(changeDTO.getNewStock()).append("'");
    }
    if (changeDTO.getOldCategory() != null || changeDTO.getNewCategory() != null) {
      messageBuilder.append(", Category van '").append(changeDTO.getOldCategory()).append("' naar '")
          .append(changeDTO.getNewCategory()).append("'");
    }

    logger.info(messageBuilder.toString());
  }
}
