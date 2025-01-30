package pl.excellentapp.brewery.beer.domain.event;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
public class BrewBeerEvent extends BeerEvent {

    public UUID getBeerId() {
        return super.getBeer().getId();
    }
}
