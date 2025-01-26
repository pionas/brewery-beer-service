package pl.excellentapp.brewery.beer.infrastructure.rest.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import pl.excellentapp.brewery.beer.domain.BeerStyleEnum;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeerResponse;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeersResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BeerRestControllerIT extends AbstractIT {

    private final String BEERS_API_URL = "/api/v1/beers";

    @AfterEach
    void clearDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "beers");
    }

    @Test
    void shouldReturnEmptyListOfBeers() {
        // given

        // when
        final var response = restTemplate.getForEntity(BEERS_API_URL, BeersResponse.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        final var beersResponse = responseBody.getBeers();
        assertNotNull(beersResponse);
        assertTrue(beersResponse.isEmpty());
    }

    @Test
    @Sql({"/db/beers.sql"})
    void shouldReturnListOfBeers() {
        // given

        // when
        final var response = restTemplate.getForEntity(BEERS_API_URL, BeersResponse.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        final var beersResponse = responseBody.getBeers();
        assertNotNull(beersResponse);
        assertFalse(beersResponse.isEmpty());
    }

    @Test
    @Sql({"/db/beers.sql"})
    void shouldReturnBeer() {
        // given
        final var beerId = UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b");

        // when
        final var response = restTemplate.getForEntity(BEERS_API_URL + "/{beerId}", BeerResponse.class, beerId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(beerId, responseBody.getId());
        assertEquals("Classic Lager", responseBody.getBeerName());
        assertEquals(BeerStyleEnum.LAGER, responseBody.getBeerStyle());
        assertEquals("1234567890", responseBody.getUpc());
        assertEquals(10, responseBody.getMinOnHand());
        assertEquals(50, responseBody.getQuantityToBrew());
        assertEquals(BigDecimal.valueOf(8.49), responseBody.getPrice());
        assertEquals(1, responseBody.getVersion());
        assertEquals(LocalDateTime.of(2025, 1, 24, 14, 0, 0, 0), responseBody.getCreatedDate());
    }

    @Test
    void shouldThrowNotFoundWhenBeerByIdNotExists() {
        // given
        final var beerId = UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b");

        // when
        final var response = restTemplate.exchange(
                BEERS_API_URL + "/{beerId}",
                HttpMethod.DELETE,
                null,
                Map.class,
                beerId
        );

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql({"/db/beers.sql"})
    void shouldDelete() {
        // given
        final var beerId = UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b");

        // when
        final var response = restTemplate.exchange(
                BEERS_API_URL + "/{beerId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                beerId
        );

        // then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        final var responseVerify = restTemplate.getForEntity(BEERS_API_URL + "/{beerId}", BeerResponse.class, beerId);
        assertEquals(HttpStatus.NOT_FOUND, responseVerify.getStatusCode());
    }

    @Test
    void shouldThrowNotFoundWhenTryDeleteBeerButBeerByIdNotExists() {
        // given
        final var beerId = UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b");

        // when
        final var response = restTemplate.exchange(
                BEERS_API_URL + "/{beerId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                beerId
        );

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}