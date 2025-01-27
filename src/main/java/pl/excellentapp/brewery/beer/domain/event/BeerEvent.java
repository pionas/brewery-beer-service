package pl.excellentapp.brewery.beer.domain.event;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.excellentapp.brewery.beer.domain.beer.Beer;

@SuperBuilder
@Getter
abstract public class BeerEvent {

    private Beer beer;
}
