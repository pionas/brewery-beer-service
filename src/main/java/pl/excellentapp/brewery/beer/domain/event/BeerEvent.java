package pl.excellentapp.brewery.beer.domain.event;

import lombok.Builder;
import lombok.Getter;
import pl.excellentapp.brewery.beer.domain.beer.Beer;

@Builder
@Getter
abstract public class BeerEvent {

    private Beer beer;
}
