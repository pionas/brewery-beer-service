package pl.excellentapp.brewery.beer.infrastructure.beerInventory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.excellentapp.brewery.beer.application.brewing.BrewingEventPublisher;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventoryService;
import pl.excellentapp.brewery.beer.domain.event.BrewingEventChannel;

@Configuration(proxyBeanMethods = false)
class BeerInventoryConfiguration {

    @Bean
    BeerInventoryService beerInventoryService(BeerInventoryFeignClient beerInventoryFeignClient) {
        return new BeerInventoryClient(beerInventoryFeignClient);
    }

    @Bean
    BrewingEventPublisher brewingEventPublisher(BrewingEventChannel eventChannel) {
        return new BrewingEventPublisher(eventChannel);
    }
}
