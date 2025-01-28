package pl.excellentapp.brewery.beer.application.brewing;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Getter
public class BeerInventoryEvent implements Serializable {

    private UUID beerId;
    private Integer stock;
}
