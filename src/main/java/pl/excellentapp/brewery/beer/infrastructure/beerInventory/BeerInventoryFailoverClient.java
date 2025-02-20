package pl.excellentapp.brewery.beer.infrastructure.beerInventory;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
public class BeerInventoryFailoverClient implements BeerInventoryFeignClient {

    private final BeerInventoryFailoverFeignClient beerInventoryFailoverFeignClient;

    @Override
    public ResponseEntity<BeerInventoryResponse> getOnHandInventory(UUID beerId) {
        return beerInventoryFailoverFeignClient.getOnHandInventory();
    }
}
