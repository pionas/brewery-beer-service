package pl.excellentapp.brewery.beer.infrastructure.eventchannel.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import pl.excellentapp.brewery.beer.domain.event.BrewingEventChannel;
import pl.excellentapp.brewery.model.events.BeerInventoryEvent;

@Component
@Slf4j
class SpringBrewingEventChannel implements BrewingEventChannel {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    private final String inventoryAddStockQueueName;

    SpringBrewingEventChannel(JmsTemplate jmsTemplate, ObjectMapper objectMapper, @Value("${queue.inventory.add-stock}") String inventoryAddStockQueueName) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
        this.inventoryAddStockQueueName = inventoryAddStockQueueName;
    }

    @Override
    public void publish(BeerInventoryEvent event) {
        try {
            final var receviedMsg = jmsTemplate.sendAndReceive(inventoryAddStockQueueName, session -> {
                try {
                    final var helloMessage = session.createTextMessage(objectMapper.writeValueAsString(event));
                    helloMessage.setStringProperty("_type", event.getClass().getName());
                    return helloMessage;

                } catch (JsonProcessingException e) {
                    throw new JMSException("boom");
                }
            });
            log.info("Receive Message: {}", receviedMsg.getBody(String.class));
        } catch (Exception exception) {
            log.error("Error publishing beer inventory event", exception);
        }
    }
}
