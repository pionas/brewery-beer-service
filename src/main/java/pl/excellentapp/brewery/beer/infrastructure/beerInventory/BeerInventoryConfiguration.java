package pl.excellentapp.brewery.beer.infrastructure.beerInventory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventoryService;

@Configuration(proxyBeanMethods = false)
class BeerInventoryConfiguration {

    @Bean
    BeerInventoryService beerInventoryService(RestTemplateBuilder restTemplateBuilder, @Value("${rest.inventory.service}") String url) {
        return new BeerInventoryClient(restTemplateBuilder.build(), url);
    }
}
