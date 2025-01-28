package pl.excellentapp.brewery.beer.application.brewing;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jms.core.JmsTemplate;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.beer.domain.beer.BeerRepository;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventory;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventoryService;
import pl.excellentapp.brewery.beer.domain.event.BrewBeerEvent;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class BrewingServiceTest {

    private static final String BREWING_QUEUE_NAME = "beer-brewing";
    private final BeerRepository beerRepository = Mockito.mock(BeerRepository.class);
    private final BeerInventoryService beerInventoryService = Mockito.mock(BeerInventoryService.class);
    private final JmsTemplate jmsTemplate = Mockito.mock(JmsTemplate.class);

    private final BrewingService brewingService = new BrewingService(beerRepository, beerInventoryService, jmsTemplate, BREWING_QUEUE_NAME);


    @Test
    void shouldSendBrewingEventWhenInventoryIsLow() {
        // given
        final var beer = Beer.builder()
                .id(UUID.fromString("0d062063-e925-4300-a046-33eddbc53fb5"))
                .beerName("Test Beer")
                .minOnHand(10)
                .build();
        final var inventory = new BeerInventory(beer.getId(), 5);
        when(beerRepository.findAll()).thenReturn(List.of(beer));
        when(beerInventoryService.getOnHandInventory(beer.getId())).thenReturn(inventory);

        // when
        brewingService.checkForLowInventory();

        // then
        verify(jmsTemplate).convertAndSend(eq(BREWING_QUEUE_NAME), any(BrewBeerEvent.class));
    }

    @Test
    void shouldNotSendBrewingEventWhenInventoryIsSufficient() {
        // given
        final var beer = Beer.builder()
                .id(UUID.fromString("0d062063-e925-4300-a046-33eddbc53fb5"))
                .beerName("Test Beer")
                .minOnHand(10)
                .build();
        final var inventory = new BeerInventory(beer.getId(), 15);
        when(beerRepository.findAll()).thenReturn(List.of(beer));
        when(beerInventoryService.getOnHandInventory(beer.getId())).thenReturn(inventory);

        // when
        brewingService.checkForLowInventory();

        // then
        verifyNoInteractions(jmsTemplate);
    }

}