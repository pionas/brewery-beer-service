package pl.excellentapp.brewery.beer.domain.beerInventory;

import java.util.UUID;

public interface BeerInventoryService {

    BeerInventory getOnHandInventory(UUID beerId);
}
