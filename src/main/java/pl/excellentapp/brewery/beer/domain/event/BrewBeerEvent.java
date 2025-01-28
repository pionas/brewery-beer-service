package pl.excellentapp.brewery.beer.domain.event;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
public class BrewBeerEvent extends BeerEvent {

    public UUID getBeerId() {
        return super.getBeer().getId();
    }
}
