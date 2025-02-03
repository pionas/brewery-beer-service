package pl.excellentapp.brewery.beer.domain.event;

import pl.excellentapp.brewery.model.events.BeerInventoryEvent;

public interface BrewingEventChannel {

    void publish(BeerInventoryEvent event);
}
