package pl.excellentapp.brewery.beer.application.brewing;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.beer.domain.beer.BeerRepository;
import pl.excellentapp.brewery.beer.domain.event.BrewBeerEvent;
import pl.excellentapp.brewery.beer.domain.exception.BeerNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BrewingListenerTest {

    private final BrewingEventPublisher brewingEventPublisher = Mockito.mock(BrewingEventPublisher.class);
    private final BeerRepository beerRepository = Mockito.mock(BeerRepository.class);

    private final BrewingListener brewingListener = new BrewingListener(brewingEventPublisher, beerRepository);

    @Test
    void shouldProcessBrewingEventWhenBeerExists() {
        // given
        final var beerId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");
        final var beer = getBeer(beerId);
        final var brewBeerEvent = getBrewBeerEvent(beer);

        when(beerRepository.findById(beerId)).thenReturn(Optional.of(beer));

        // when
        brewingListener.brewing(brewBeerEvent);

        // then
        ArgumentCaptor<Beer> beerCaptor = ArgumentCaptor.forClass(Beer.class);
        verify(beerRepository).save(beerCaptor.capture());
        verify(brewingEventPublisher).publishBrewingEvent(eq(beer), anyInt());

        final var savedBeer = beerCaptor.getValue();
        assertEquals(beer, savedBeer);
        assertTrue(savedBeer.getQuantityToBrew() >= beer.getMinOnHand());
    }

    @Test
    void shouldThrowExceptionWhenBeerNotFound() {
        // given
        final var beerId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");
        final var beer = getBeer(beerId);
        final var brewBeerEvent = getBrewBeerEvent(beer);

        when(beerRepository.findById(beerId)).thenReturn(Optional.empty());

        // when
        assertThrows(BeerNotFoundException.class, () -> brewingListener.brewing(brewBeerEvent));

        // then
        verifyNoInteractions(brewingEventPublisher);
        verify(beerRepository, never()).save(any());
    }

    @Test
    void shouldPublishCorrectStockValue() {
        // given
        final var beerId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");
        final var beer = Beer.builder()
                .id(beerId)
                .minOnHand(150)
                .build();
        final var brewBeerEvent = getBrewBeerEvent(beer);
        when(beerRepository.findById(beerId)).thenReturn(Optional.of(beer));

        // when
        brewingListener.brewing(brewBeerEvent);

        // then
        verify(brewingEventPublisher).publishBrewingEvent(any(), anyInt());
    }

    private Beer getBeer(UUID beerId) {
        return Beer.builder()
                .id(beerId)
                .minOnHand(100)
                .build();
    }

    private BrewBeerEvent getBrewBeerEvent(Beer beer) {
        return BrewBeerEvent.builder()
                .beer(beer)
                .build();
    }
}