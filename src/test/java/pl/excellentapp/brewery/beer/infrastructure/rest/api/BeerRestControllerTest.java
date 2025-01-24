package pl.excellentapp.brewery.beer.infrastructure.rest.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.excellentapp.brewery.beer.application.BeerService;
import pl.excellentapp.brewery.beer.domain.Beer;
import pl.excellentapp.brewery.beer.domain.BeerStyleEnum;
import pl.excellentapp.brewery.beer.domain.exception.BeerNotFoundException;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeerRequest;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeerResponse;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeersResponse;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.mapper.BeerRestMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BeerRestControllerTest extends AbstractMvcTest {

    private static final OffsetDateTime OFFSET_DATE_TIME = OffsetDateTime.of(2025, 1, 23, 12, 7, 0, 0, ZoneOffset.UTC);

    @InjectMocks
    private BeerRestController controller;

    @Mock
    private BeerService beerService;

    @Spy
    private BeerRestMapper beerRestMapper = Mappers.getMapper(BeerRestMapper.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void getBeers_ShouldReturnEmptyListOfBeers() throws Exception {
        // given

        // when
        mockMvc.perform(get("/api/v1/beers"))
                .andExpect(status().isOk());

        // then
        verify(beerService).findAll();
    }

    @Test
    void getBeers_ShouldReturnListOfBeers() throws Exception {
        // given
        final var beer1 = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer1", BeerStyleEnum.IPA, "12345", 1, 11, BigDecimal.valueOf(11.99), 1L, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
        final var beer2 = createBeer(UUID.fromString("4a5b96de-684a-411b-9616-fddd0b06a382"), "Test Beer2", BeerStyleEnum.GOSE, "67890", 2, 12, BigDecimal.valueOf(12.99), 1L, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
        when(beerService.findAll())
                .thenReturn(List.of(beer1, beer2));

        // when
        final var response = mockMvc.perform(get("/api/v1/beers"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var beersResponse = super.mapFromJson(responseBody, BeersResponse.class);
        assertNotNull(beersResponse);
        final var beersResponseList = beersResponse.getBeers();
        assertNotNull(beersResponseList);
        assertEquals(2, beersResponseList.size());
        final var beerResponse1 = beersResponseList.getFirst();
        assertNotNull(beerResponse1);
        assertEquals(beerResponse1.getBeerName(), beer1.getBeerName());
        assertEquals(beerResponse1.getBeerStyle(), beer1.getBeerStyle());
        assertEquals(beerResponse1.getUpc(), beer1.getUpc());
        assertEquals(beerResponse1.getMinOnHand(), beer1.getMinOnHand());
        assertEquals(beerResponse1.getQuantityToBrew(), beer1.getQuantityToBrew());
        assertEquals(beerResponse1.getPrice(), beer1.getPrice());
        assertEquals(beerResponse1.getVersion(), beer1.getVersion());
        assertEquals(beerResponse1.getCreatedDate(), beer1.getCreatedDate());
        final var beerResponse2 = beersResponseList.getLast();
        assertNotNull(beerResponse2);
        assertEquals(beerResponse2.getBeerName(), beer2.getBeerName());
        assertEquals(beerResponse2.getBeerStyle(), beer2.getBeerStyle());
        assertEquals(beerResponse2.getUpc(), beer2.getUpc());
        assertEquals(beerResponse2.getMinOnHand(), beer2.getMinOnHand());
        assertEquals(beerResponse2.getQuantityToBrew(), beer2.getQuantityToBrew());
        assertEquals(beerResponse2.getPrice(), beer2.getPrice());
        assertEquals(beerResponse2.getVersion(), beer2.getVersion());
        assertEquals(beerResponse2.getCreatedDate(), beer2.getCreatedDate());
        verify(beerService).findAll();
    }

    @Test
    void getBeer_ShouldReturnNotFound() throws Exception {
        // given
        final var beerId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");
        // when
        final var response = mockMvc.perform(get("/api/v1/beers/" + beerId))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        verify(beerService).findById(beerId);
    }

    @Test
    void getBeer_ShouldReturn() throws Exception {
        // given
        final var beerId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");
        final var beer = createBeer(beerId, "Test Beer1", BeerStyleEnum.IPA, "12345", 1, 11, BigDecimal.valueOf(11.99), 1L, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
        when(beerService.findById(beerId)).thenReturn(Optional.of(beer));

        // when
        final var response = mockMvc.perform(get("/api/v1/beers/" + beerId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var beerResponse = super.mapFromJson(responseBody, BeerResponse.class);
        assertNotNull(beerResponse);
        assertEquals(beerResponse.getBeerName(), beer.getBeerName());
        assertEquals(beerResponse.getBeerStyle(), beer.getBeerStyle());
        assertEquals(beerResponse.getUpc(), beer.getUpc());
        assertEquals(beerResponse.getMinOnHand(), beer.getMinOnHand());
        assertEquals(beerResponse.getQuantityToBrew(), beer.getQuantityToBrew());
        assertEquals(beerResponse.getPrice(), beer.getPrice());
        assertEquals(beerResponse.getVersion(), beer.getVersion());
        assertEquals(beerResponse.getCreatedDate(), beer.getCreatedDate());
    }

    @Test
    void createBeer_ShouldReturnCreatedBeer() throws Exception {
        // given
        final var beer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer1", BeerStyleEnum.IPA, "12345", 1, 11, BigDecimal.valueOf(11.99), 1L, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
        final var beerRequest = BeerRequest.builder()
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .minOnHand(beer.getMinOnHand())
                .quantityToBrew(beer.getQuantityToBrew())
                .price(beer.getPrice())
                .build();
        when(beerService.create(any())).thenReturn(beer);

        // when
        final var response = mockMvc.perform(post("/api/v1/beers")
                        .content(super.mapToJson(beerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var beerResponse = super.mapFromJson(responseBody, BeerResponse.class);
        assertNotNull(beerResponse);
        assertEquals(beerResponse.getId(), beer.getId());
        assertEquals(beerResponse.getBeerName(), beer.getBeerName());
        assertEquals(beerResponse.getBeerStyle(), beer.getBeerStyle());
        assertEquals(beerResponse.getUpc(), beer.getUpc());
        assertEquals(beerResponse.getMinOnHand(), beer.getMinOnHand());
        assertEquals(beerResponse.getQuantityToBrew(), beer.getQuantityToBrew());
        assertEquals(beerResponse.getPrice(), beer.getPrice());
        assertEquals(beerResponse.getVersion(), beer.getVersion());
        assertEquals(beerResponse.getCreatedDate(), beer.getCreatedDate());
        verify(beerService).create(any());
    }

    @Test
    void updateBeer_ShouldReturnUpdatedBeer() throws Exception {
        // given
        final var offsetDateTime = OffsetDateTime.of(2025, 1, 23, 12, 7, 10, 0, ZoneOffset.UTC);
        final var originalBeer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer", BeerStyleEnum.ALE, "12345", 5, 10, BigDecimal.valueOf(10.99), 1L, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
        final var updateRequest = getUpdateRequest(originalBeer);
        final var expectedBeer = getExpectedBeer(originalBeer, offsetDateTime);
        final var beerRequest = BeerRequest.builder()
                .beerName(updateRequest.getBeerName())
                .beerStyle(updateRequest.getBeerStyle())
                .upc(updateRequest.getUpc())
                .minOnHand(updateRequest.getMinOnHand())
                .quantityToBrew(updateRequest.getQuantityToBrew())
                .price(updateRequest.getPrice())
                .build();
        when(beerService.update(any(), any())).thenReturn(expectedBeer);

        // when
        final var response = mockMvc.perform(put("/api/v1/beers/" + originalBeer.getId())
                        .content(super.mapToJson(beerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var beerResponse = super.mapFromJson(responseBody, BeerResponse.class);
        assertNotNull(beerResponse);
        assertEquals(beerResponse.getId(), expectedBeer.getId());
        assertEquals(beerResponse.getBeerName(), expectedBeer.getBeerName());
        assertEquals(beerResponse.getBeerStyle(), expectedBeer.getBeerStyle());
        assertEquals(beerResponse.getUpc(), expectedBeer.getUpc());
        assertEquals(beerResponse.getMinOnHand(), expectedBeer.getMinOnHand());
        assertEquals(beerResponse.getQuantityToBrew(), expectedBeer.getQuantityToBrew());
        assertEquals(beerResponse.getPrice(), expectedBeer.getPrice());
        assertEquals(beerResponse.getVersion(), expectedBeer.getVersion());
        assertEquals(beerResponse.getCreatedDate(), expectedBeer.getCreatedDate());
        verify(beerService).update(any(), any());
    }

    @Test
    void deleteBeer_ShouldDeletedBeer() throws Exception {
        // given
        final var beerId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");

        // when
        mockMvc.perform(delete("/api/v1/beers/" + beerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        // then
        verify(beerService).delete(beerId);
    }

    @Test
    void deleteBeer_ShouldThrowExceptionWhenTryDeletedBeer() throws Exception {
        // given
        final var beerId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");
        doThrow(new BeerNotFoundException("Beer Not Found. UUID: " + beerId)).when(beerService).delete(beerId);

        // when
        mockMvc.perform(delete("/api/v1/beers/" + beerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // then
        verify(beerService).delete(beerId);
    }

    private Beer createBeer(UUID id, String beerName, BeerStyleEnum beerStyle, String upc, int minOnHand, int quantityToBrew, BigDecimal price, long version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate) {
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

    private Beer getUpdateRequest(Beer originalBeer) {
        return createBeer(
                originalBeer.getId(),
                "Updated Beer",
                originalBeer.getBeerStyle(),
                originalBeer.getUpc(),
                originalBeer.getMinOnHand(),
                originalBeer.getQuantityToBrew(),
                originalBeer.getPrice(),
                originalBeer.getVersion(),
                originalBeer.getCreatedDate(),
                originalBeer.getLastModifiedDate()
        );
    }

    private Beer getExpectedBeer(Beer originalBeer, OffsetDateTime offsetDateTime) {
        return createBeer(
                originalBeer.getId(),
                "Updated Beer",
                originalBeer.getBeerStyle(),
                originalBeer.getUpc(),
                originalBeer.getMinOnHand(),
                originalBeer.getQuantityToBrew(),
                originalBeer.getPrice(),
                originalBeer.getVersion(),
                originalBeer.getCreatedDate(),
                offsetDateTime
        );
    }
}