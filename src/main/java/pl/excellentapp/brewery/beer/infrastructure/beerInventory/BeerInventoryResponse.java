package pl.excellentapp.brewery.beer.infrastructure.beerInventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventory;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class BeerInventoryResponse {

    private UUID beerId;
    private int availableStock;

    public BeerInventory toBeerInventory() {
        return new BeerInventory(beerId, availableStock);
    }
}
