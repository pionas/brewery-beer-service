package pl.excellentapp.brewery.beer.application;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.excellentapp.brewery.beer.application.beer.BeerService;
import pl.excellentapp.brewery.beer.application.beer.BeerServiceImpl;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.beer.domain.beer.BeerRepository;
import pl.excellentapp.brewery.beer.domain.beer.BeerStyleEnum;
import pl.excellentapp.brewery.beer.domain.exception.BeerNotFoundException;
import pl.excellentapp.brewery.beer.utils.DateTimeProvider;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeerServiceTest {

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2025, 1, 23, 12, 7, 0, 0);

    private final BeerRepository beerRepository = Mockito.mock(BeerRepository.class);
    private final DateTimeProvider dateTimeProvider = Mockito.mock(DateTimeProvider.class);

    private final BeerService beerService = new BeerServiceImpl(beerRepository, dateTimeProvider);


    @Test
    void findAll_ShouldReturnListOfBeers() {
        // given
        final var beers = List.of(
                createBeer(UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b"), "Classic Lager", BeerStyleEnum.LAGER, "1234567890", 10, 50, BigDecimal.valueOf(8.49), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME),
                createBeer(UUID.fromString("2c4f2ed6-bd1d-4f9d-82c6-6b975b5cf5b3"), "Golden Pilsner", BeerStyleEnum.PILSNER, "2345678901", 12, 40, BigDecimal.valueOf(9.99), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME),
                createBeer(UUID.fromString("3a8e0e2f-587d-4b3c-b1c9-27f5d6c3627a"), "Dark Stout", BeerStyleEnum.STOUT, "3456789012", 8, 30, BigDecimal.valueOf(11.99), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME),
                createBeer(UUID.fromString("4c9e7a3b-84e7-4f8e-95e2-cd2f1d56e6b7"), "Salty Gose", BeerStyleEnum.GOSE, "4567890123", 5, 25, BigDecimal.valueOf(10.49), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME),
                createBeer(UUID.fromString("5d3f8e7c-9f2b-42e1-908d-cf3d1e678e9b"), "Robust Porter", BeerStyleEnum.PORTER, "5678901234", 6, 20, BigDecimal.valueOf(12.49), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME),
                createBeer(UUID.fromString("6e8f9d4c-7c8a-45d1-8b4c-ed3f5a7b6e9d"), "Amber Ale", BeerStyleEnum.ALE, "6789012345", 9, 35, BigDecimal.valueOf(10.99), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME),
                createBeer(UUID.fromString("7f1b3c2d-8e9f-41b2-94c8-ef3d7a6b5c9f"), "Hefeweizen Wheat", BeerStyleEnum.WHEAT, "7890123456", 7, 28, BigDecimal.valueOf(9.49), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME),
                createBeer(UUID.fromString("8a2d4e6f-9b3c-4e2f-b7d1-2c3f5a8e7b6d"), "Hoppy IPA", BeerStyleEnum.IPA, "8901234567", 8, 32, BigDecimal.valueOf(11.79), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME),
                createBeer(UUID.fromString("9c3e5d7a-b8f2-41c3-82e9-f2b1d6e5c4f7"), "Crisp Pale Ale", BeerStyleEnum.PALE_ALE, "9012345678", 10, 37, BigDecimal.valueOf(10.29), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME),
                createBeer(UUID.fromString("0d1e2f3b-5a7c-4d1f-8e9b-2f3d6a8b7c5f"), "Rustic Saison", BeerStyleEnum.SAISON, "0123456789", 4, 18, BigDecimal.valueOf(13.99), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME)
        );
        when(beerRepository.findAll()).thenReturn(beers);

        // when
        final var result = beerService.findAll();

        // then
        assertEquals(10, result.size());
        assertEquals(beers, result);
        verify(beerRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnBeer_WhenBeerExists() {
        // given
        final var beer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer", BeerStyleEnum.ALE, "12345", 5, 10, BigDecimal.valueOf(10.99), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME);
        when(beerRepository.findById(beer.getId())).thenReturn(Optional.of(beer));

        // when
        final var result = beerService.findById(beer.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(beer, result.get());
        verify(beerRepository, times(1)).findById(beer.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenBeerNotFound() {
        // given
        final var beer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer", BeerStyleEnum.ALE, "12345", 5, 10, BigDecimal.valueOf(10.99), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME);
        when(beerRepository.findById(beer.getId())).thenReturn(Optional.empty());

        // when
        final var beerOptional = beerService.findById(beer.getId());

        // then
        assertTrue(beerOptional.isEmpty());
        verify(beerRepository, times(1)).findById(beer.getId());
    }

    @Test
    void create_ShouldSaveAndReturnBeer() {
        // given
        final var beer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer", BeerStyleEnum.ALE, "12345", 5, 10, BigDecimal.valueOf(10.99), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME);
        when(beerRepository.save(beer)).thenReturn(beer);

        // when
        final var result = beerService.create(beer);

        // then
        assertEquals(beer, result);
        verify(beerRepository, times(1)).save(beer);
    }

    @Test
    void update_ShouldUpdateAndReturnBeer() {
        // given
        final var localDateTime = LocalDateTime.of(2025, 1, 23, 12, 7, 10, 0);
        final var originalBeer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer", BeerStyleEnum.ALE, "12345", 5, 10, BigDecimal.valueOf(10.99), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME);
        final var updateRequest = getUpdateRequest(originalBeer);
        final var expectedBeer = getExpectedBeer(updateRequest, localDateTime);

        when(beerRepository.findById(originalBeer.getId())).thenReturn(Optional.of(originalBeer));
        when(beerRepository.save(any(Beer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(dateTimeProvider.now()).thenReturn(localDateTime);

        // when
        final var result = beerService.update(originalBeer.getId(), updateRequest);

        // then
        assertEquals(expectedBeer, result);
        assertEquals(expectedBeer.getId(), result.getId());
        assertEquals(expectedBeer.getBeerName(), result.getBeerName());
        assertEquals(expectedBeer.getBeerStyle(), result.getBeerStyle());
        assertEquals(expectedBeer.getUpc(), result.getUpc());
        assertEquals(expectedBeer.getMinOnHand(), result.getMinOnHand());
        assertEquals(expectedBeer.getQuantityToBrew(), result.getQuantityToBrew());
        assertEquals(expectedBeer.getPrice(), result.getPrice());
        assertEquals(expectedBeer.getVersion(), result.getVersion());
        assertEquals(expectedBeer.getCreatedDate(), result.getCreatedDate());
        assertEquals(localDateTime, result.getLastModifiedDate());
        verify(beerRepository, times(1)).findById(originalBeer.getId());
        verify(beerRepository, times(1)).save(originalBeer); // Upewnij się, że zapisujemy zaktualizowany obiekt
    }


    @Test
    void delete_ShouldDeleteBeer_WhenBeerExists() {
        // given
        final var beer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer", BeerStyleEnum.ALE, "12345", 5, 10, BigDecimal.valueOf(10.99), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME);
        when(beerRepository.findById(beer.getId())).thenReturn(Optional.of(beer));
        doNothing().when(beerRepository).deleteById(beer.getId());

        // when
        assertDoesNotThrow(() -> beerService.delete(beer.getId()));

        // then
        verify(beerRepository, times(1)).findById(beer.getId());
        verify(beerRepository, times(1)).deleteById(beer.getId());
    }

    @Test
    void delete_ShouldThrowException_WhenBeerNotFound() {
        // given
        final var beer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer", BeerStyleEnum.ALE, "12345", 5, 10, BigDecimal.valueOf(10.99), 1L, LOCAL_DATE_TIME, LOCAL_DATE_TIME);
        when(beerRepository.findById(beer.getId())).thenReturn(Optional.empty());

        // when
        assertThrows(BeerNotFoundException.class, () -> beerService.delete(beer.getId()));

        // then
        verify(beerRepository, times(1)).findById(beer.getId());
        verify(beerRepository, never()).deleteById(any());
    }

    private Beer getUpdateRequest(Beer originalBeer) {
        final var price = originalBeer.getPrice().add(new BigDecimal(1));
        return createBeer(
                originalBeer.getId(),
                originalBeer.getBeerName() + "X",
                BeerStyleEnum.IPA,
                originalBeer.getUpc() + "A",
                2 * originalBeer.getMinOnHand(),
                2 * originalBeer.getQuantityToBrew(),
                price,
                originalBeer.getVersion() + 1,
                originalBeer.getCreatedDate(),
                originalBeer.getLastModifiedDate()
        );
    }

    private Beer getExpectedBeer(Beer originalBeer, LocalDateTime LocalDateTime) {
        return createBeer(
                originalBeer.getId(),
                originalBeer.getBeerName(),
                originalBeer.getBeerStyle(),
                originalBeer.getUpc(),
                originalBeer.getMinOnHand(),
                originalBeer.getQuantityToBrew(),
                originalBeer.getPrice(),
                originalBeer.getVersion(),
                originalBeer.getCreatedDate(),
                LocalDateTime
        );
    }

    private Beer createBeer(UUID id, String beerName, BeerStyleEnum beerStyle, String upc, int minOnHand, int quantityToBrew, BigDecimal price, long version, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        return Beer.builder()
                .id(id)
                .beerName(beerName)
                .beerStyle(beerStyle)
                .upc(upc)
                .minOnHand(minOnHand)
                .quantityToBrew(quantityToBrew)
                .price(price)
                .version(version)
                .createdDate(createdDate)
                .lastModifiedDate(lastModifiedDate)
                .build();
    }
}