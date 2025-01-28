package pl.excellentapp.brewery.beer.application.brewing;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import pl.excellentapp.brewery.beer.domain.beer.BeerRepository;
import pl.excellentapp.brewery.beer.domain.event.BrewBeerEvent;
import pl.excellentapp.brewery.beer.domain.exception.BeerNotFoundException;

import java.util.Random;

@Service
class BrewingListener {

    private static final Random BEER_STOCK_RANDOM_GENERATOR = new Random();
    private final BrewingEventPublisher brewingEventPublisher;
    private final BeerRepository beerRepository;

    BrewingListener(BrewingEventPublisher brewingEventPublisher, BeerRepository beerRepository) {
        this.brewingEventPublisher = brewingEventPublisher;
        this.beerRepository = beerRepository;
    }

    @JmsListener(destination = "${queue.beer.brewing}")
    public void brewing(@Payload BrewBeerEvent brewBeerEvent) {
        final var beer = beerRepository.findById(brewBeerEvent.getBeerId())
                .orElseThrow(() -> new BeerNotFoundException("Beer Not Found. UUID: " + brewBeerEvent.getBeerId()));
        final var stock = BEER_STOCK_RANDOM_GENERATOR.nextInt(300) + beer.getMinOnHand();
        beer.addQuantityToBrew(stock);
        beerRepository.save(beer);
        brewingEventPublisher.publishBrewingEvent(beer, stock);
    }

}
