package pl.excellentapp.brewery.model.events;

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
