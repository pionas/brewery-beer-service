package pl.excellentapp.brewery.beer.domain.beerInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeerInventory {

    private UUID id;
    private int availableStock;

    public static BeerInventory empty(UUID beerId) {
        return new BeerInventory(beerId, 0);
    }
}
