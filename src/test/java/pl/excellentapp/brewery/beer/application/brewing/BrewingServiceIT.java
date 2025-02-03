package pl.excellentapp.brewery.beer.application.brewing;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventory;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventoryService;
import pl.excellentapp.brewery.model.events.BrewBeerEvent;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.AbstractIT;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BrewingServiceIT extends AbstractIT {

    @Autowired
    private BrewingService brewingService;

    @MockitoBean
    private BeerInventoryService beerInventoryService;

    @MockitoBean
    private JmsTemplate jmsTemplate;

    @Value("${queue.beer.brewing}")
    private String brewingQueueName;

    @Test
    @Sql({"/db/beers_one.sql"})
    void shouldSendBrewingEventForLowInventory() {
        // given
        final var inventory = new BeerInventory(UUID.fromString("0d062063-e925-4300-a046-33eddbc53fb5"), 5);
        when(beerInventoryService.getOnHandInventory(any())).thenReturn(inventory);

        // when
        brewingService.checkForLowInventory();

        // then
        ArgumentCaptor<BrewBeerEvent> eventCaptor = ArgumentCaptor.forClass(BrewBeerEvent.class);
        verify(jmsTemplate).convertAndSend(eq(brewingQueueName), eventCaptor.capture());
    }
}