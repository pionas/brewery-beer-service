package pl.excellentapp.brewery.beer.domain.event;

import pl.excellentapp.brewery.beer.application.brewing.BeerInventoryEvent;

public interface BrewingEventChannel {

    void publish(BeerInventoryEvent event);
}
