package pl.excellentapp.brewery.beer.application.brewing;

import lombok.RequiredArgsConstructor;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.beer.domain.event.BrewingEventChannel;
import pl.excellentapp.brewery.beer.events.BeerInventoryEvent;

@RequiredArgsConstructor
public class BrewingEventPublisher {

    private final BrewingEventChannel eventChannel;

    public void publishBrewingEvent(Beer beer, int stock) {
        eventChannel.publish(BeerInventoryEvent.builder()
                .beerId(beer.getId())
                .stock(stock)
                .build()
        );
    }
}
