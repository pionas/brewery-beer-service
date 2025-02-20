package pl.excellentapp.brewery.beer.infrastructure.beerInventory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventory;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventoryService;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
class BeerInventoryClient implements BeerInventoryService {

    private final BeerInventoryFeignClient beerInventoryFeignClient;

    @Override
    public BeerInventory getOnHandInventory(UUID beerId) {
        try {
            final var beerInventoryResponseResponse = beerInventoryFeignClient.getOnHandInventory(beerId);
            return Objects.requireNonNull(beerInventoryResponseResponse.getBody()).toBeerInventory();
        } catch (HttpClientErrorException.NotFound exception) {
            log.warn("Beer inventory for {} not exists", beerId, exception);
        }
        return BeerInventory.empty(beerId);
    }
}
