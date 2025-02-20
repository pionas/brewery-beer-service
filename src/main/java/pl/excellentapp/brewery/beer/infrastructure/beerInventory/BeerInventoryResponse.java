package pl.excellentapp.brewery.beer.infrastructure.beerInventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.excellentapp.brewery.beer.domain.beerInventory.BeerInventory;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BeerInventoryResponse {

    private UUID id;
    private int availableStock;

    public BeerInventory toBeerInventory() {
        return new BeerInventory(id, availableStock);
    }
}
