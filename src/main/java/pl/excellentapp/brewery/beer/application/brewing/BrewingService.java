package pl.excellentapp.brewery.beer.application.brewing;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.beer.domain.beer.BeerRepository;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventoryService;
import pl.excellentapp.brewery.beer.domain.event.BrewBeerEvent;

import java.util.List;

@Service
@Slf4j
class BrewingService {

    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final String brewingQueueName;

    public BrewingService(BeerRepository beerRepository,
                          BeerInventoryService beerInventoryService,
                          JmsTemplate jmsTemplate,
                          @Value("${queue.beer.brewing}") String brewingQueueName) {
        this.beerRepository = beerRepository;
        this.beerInventoryService = beerInventoryService;
        this.jmsTemplate = jmsTemplate;
        this.brewingQueueName = brewingQueueName;
    }

    @Scheduled(cron = "${scheduler.cron.inventory-checking}")
    @SchedulerLock(name = "TaskScheduler_checkForLowInventory", lockAtLeastFor = "${scheduler.lock-at-least-for}")
    public void checkForLowInventory() {
        List<Beer> beers = beerRepository.findAll();
        beers.forEach(beer -> {
            final var beerInventory = beerInventoryService.getOnHandInventory(beer.getId());
            log.debug("Checking Inventory for: {} / {}", beer.getBeerName(), beer.getId());
            log.debug("Min On hand is: {}", beer.getMinOnHand());
            log.debug("Inventory is: {}", beerInventory.getAvailableStock());

            if (beer.getMinOnHand() >= beerInventory.getAvailableStock()) {
                jmsTemplate.convertAndSend(brewingQueueName, BrewBeerEvent.builder().beer(beer).build());
            }
        });
    }
}
