package pl.excellentapp.brewery.beer.infrastructure.beerInventory;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventory;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventoryService;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
class BeerInventoryClient implements BeerInventoryService {

    private final RestTemplate restTemplate;
    private final String url;

    @Override
    public BeerInventory getOnHandInventory(UUID beerId) {
        return Objects.requireNonNull(restTemplate.getForObject(url, BeerInventoryResponse.class, beerId))
                .toBeerInventory();
    }
}
