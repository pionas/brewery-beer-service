package pl.excellentapp.brewery.model.events;

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
