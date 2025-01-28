package pl.excellentapp.brewery.beer.infrastructure.eventchannel.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import pl.excellentapp.brewery.beer.application.brewing.BeerInventoryEvent;
import pl.excellentapp.brewery.beer.domain.event.BrewingEventChannel;

@Component
class SpringBrewingEventChannel implements BrewingEventChannel {

    private final JmsTemplate jmsTemplate;
    private final String inventoryAddStockQueueName;

    SpringBrewingEventChannel(JmsTemplate jmsTemplate, @Value("${queue.inventory.add-stock}") String inventoryAddStockQueueName) {
        this.jmsTemplate = jmsTemplate;
        this.inventoryAddStockQueueName = inventoryAddStockQueueName;
    }

    @Override
    public void publish(BeerInventoryEvent event) {
        jmsTemplate.convertAndSend(inventoryAddStockQueueName, event);
    }
}
