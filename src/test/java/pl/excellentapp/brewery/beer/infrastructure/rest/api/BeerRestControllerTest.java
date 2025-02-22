package pl.excellentapp.brewery.beer.infrastructure.rest.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.excellentapp.brewery.beer.application.beer.BeerPage;
import pl.excellentapp.brewery.beer.application.beer.BeerService;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.beer.domain.exception.BeerNotFoundException;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeerRequest;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.mapper.BeerRestMapper;
import pl.excellentapp.brewery.beer.infrastructure.rest.handler.GlobalExceptionHandler;
import pl.excellentapp.brewery.beer.utils.DateTimeProvider;
import pl.excellentapp.brewery.model.BeerDto;
import pl.excellentapp.brewery.model.BeerPagedList;
import pl.excellentapp.brewery.model.BeerStyle;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
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

    @Mock
    private DateTimeProvider dateTimeProvider;

    @Spy
    private BeerRestMapper beerRestMapper = Mappers.getMapper(BeerRestMapper.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler(dateTimeProvider))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(getObjectMapper()))
                .build();
    }

    @Test
    void shouldReturnEmptyListOfBeers() throws Exception {
        // given

        // when
        mockMvc.perform(get("/api/v1/beers"))
                .andExpect(status().isOk());

        // then
        verify(beerService).list(any(), any(), any());
    }

    @Test
    void shouldReturnListOfBeers() throws Exception {
        // given
        final var beer1 = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer1", BeerStyle.IPA, "12345", 1, 11, BigDecimal.valueOf(11.99), 1, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
        final var beer2 = createBeer(UUID.fromString("4a5b96de-684a-411b-9616-fddd0b06a382"), "Test Beer2", BeerStyle.GOSE, "67890", 2, 12, BigDecimal.valueOf(12.99), 1, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
        BeerPage beerPage = BeerPage.of(
                Pair.of(List.of(beer1, beer2), 2),
                PageRequest.of(1, 2)
        );
        when(beerService.list(any(), any(), any()))
                .thenReturn(beerPage);

        // when
        final var response = mockMvc.perform(get("/api/v1/beers"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var beerPagedList = super.mapFromJson(responseBody, BeerPagedList.class);
        assertNotNull(beerPagedList);
        final var beersResponseList = beerPagedList.getContent();
        assertNotNull(beersResponseList);
        assertEquals(2, beersResponseList.size());
        final var beerResponse1 = beersResponseList.getFirst();
        assertNotNull(beerResponse1);
        assertEquals(beerResponse1.getBeerName(), beer1.getBeerName());
        assertEquals(beerResponse1.getBeerStyle(), beer1.getBeerStyle().name());
        assertEquals(beerResponse1.getUpc(), beer1.getUpc());
        assertEquals(beerResponse1.getMinOnHand(), beer1.getMinOnHand());
        assertEquals(beerResponse1.getQuantityToBrew(), beer1.getQuantityToBrew());
        assertEquals(beerResponse1.getPrice(), beer1.getPrice());
        assertEquals(beerResponse1.getVersion(), beer1.getVersion());
        assertEquals(beerResponse1.getCreatedDate(), beer1.getCreatedDate());
        final var beerResponse2 = beersResponseList.getLast();
        assertNotNull(beerResponse2);
        assertEquals(beerResponse2.getBeerName(), beer2.getBeerName());
        assertEquals(beerResponse2.getBeerStyle(), beer2.getBeerStyle().name());
        assertEquals(beerResponse2.getUpc(), beer2.getUpc());
        assertEquals(beerResponse2.getMinOnHand(), beer2.getMinOnHand());
        assertEquals(beerResponse2.getQuantityToBrew(), beer2.getQuantityToBrew());
        assertEquals(beerResponse2.getPrice(), beer2.getPrice());
        assertEquals(beerResponse2.getVersion(), beer2.getVersion());
        assertEquals(beerResponse2.getCreatedDate(), beer2.getCreatedDate());
        verify(beerService).list(any(), any(), any());
    }

    @Test
    void shouldReturnNotFoundWhenTryGetBeerById() throws Exception {
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
    void shouldReturnBeer() throws Exception {
        // given
        final var beerId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");
        final var beer = createBeer(beerId, "Test Beer1", BeerStyle.IPA, "12345", 1, 11, BigDecimal.valueOf(11.99), 1, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
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
        final var beerDto = super.mapFromJson(responseBody, BeerDto.class);
        assertNotNull(beerDto);
        assertEquals(beerDto.getBeerName(), beer.getBeerName());
        assertEquals(beerDto.getBeerStyle(), beer.getBeerStyle().name());
        assertEquals(beerDto.getUpc(), beer.getUpc());
        assertEquals(beerDto.getMinOnHand(), beer.getMinOnHand());
        assertEquals(beerDto.getQuantityToBrew(), beer.getQuantityToBrew());
        assertEquals(beerDto.getPrice(), beer.getPrice());
        assertEquals(beerDto.getVersion(), beer.getVersion());
        assertEquals(beerDto.getCreatedDate(), beer.getCreatedDate());
    }

    @Test
    void shouldReturnCreatedBeer() throws Exception {
        // given
        final var beer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer1", BeerStyle.IPA, "12345", 1, 11, BigDecimal.valueOf(11.99), 1, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
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
        final var beerDto = super.mapFromJson(responseBody, BeerDto.class);
        assertNotNull(beerDto);
        assertEquals(beerDto.getId(), beer.getId());
        assertEquals(beerDto.getBeerName(), beer.getBeerName());
        assertEquals(beerDto.getBeerStyle(), beer.getBeerStyle().name());
        assertEquals(beerDto.getUpc(), beer.getUpc());
        assertEquals(beerDto.getMinOnHand(), beer.getMinOnHand());
        assertEquals(beerDto.getQuantityToBrew(), beer.getQuantityToBrew());
        assertEquals(beerDto.getPrice(), beer.getPrice());
        assertEquals(beerDto.getVersion(), beer.getVersion());
        assertEquals(beerDto.getCreatedDate(), beer.getCreatedDate());
        verify(beerService).create(any());
    }

    @Test
    void shouldThrowExceptionWhenBeerRequestIsInvalid() throws Exception {
        // given
        final var beerRequest = BeerRequest.builder()
                .build();
        when(dateTimeProvider.now()).thenReturn(OFFSET_DATE_TIME);

        // when
        final var response = mockMvc.perform(post("/api/v1/beers")
                        .content(super.mapToJson(beerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        // then
        final var expectedJson = """
                    {
                       "timestamp": "2025-01-23T12:07:00Z",
                       "status": 400,
                       "errors": {
                         "beerName": ["must not be blank"],
                         "beerStyle": ["must not be null"],
                         "upc": ["must not be blank"],
                         "minOnHand": ["must not be null"],
                         "quantityToBrew": ["must not be null"],
                         "price": ["must not be null"]
                       }
                     }
                """;
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var expectedMap = super.mapFromJson(expectedJson, Map.class);
        final var actualMap = super.mapFromJson(responseBody, Map.class);
        assertEquals(expectedMap, actualMap);

        verify(beerService, never()).create(any());
    }

    @Test
    void shouldReturnUpdatedBeer() throws Exception {
        // given
        final var offsetDateTime = OffsetDateTime.of(2025, 1, 23, 12, 7, 10, 0, ZoneOffset.UTC);
        final var originalBeer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer", BeerStyle.ALE, "12345", 5, 10, BigDecimal.valueOf(10.99), 1, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
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
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var beerDto = super.mapFromJson(responseBody, BeerDto.class);
        assertNotNull(beerDto);
        assertEquals(beerDto.getId(), expectedBeer.getId());
        assertEquals(beerDto.getBeerName(), expectedBeer.getBeerName());
        assertEquals(beerDto.getBeerStyle(), expectedBeer.getBeerStyle().name());
        assertEquals(beerDto.getUpc(), expectedBeer.getUpc());
        assertEquals(beerDto.getMinOnHand(), expectedBeer.getMinOnHand());
        assertEquals(beerDto.getQuantityToBrew(), expectedBeer.getQuantityToBrew());
        assertEquals(beerDto.getPrice(), expectedBeer.getPrice());
        assertEquals(beerDto.getVersion(), expectedBeer.getVersion());
        assertEquals(beerDto.getCreatedDate(), expectedBeer.getCreatedDate());
        verify(beerService).update(any(), any());
    }

    @Test
    void shouldThrowNotFoundWhenTryUpdateBeerButBeerByIdNotExists() throws Exception {
        // given
        final var beer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer", BeerStyle.ALE, "12345", 5, 10, BigDecimal.valueOf(10.99), 1, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
        final var beerRequest = BeerRequest.builder()
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .minOnHand(beer.getMinOnHand())
                .quantityToBrew(beer.getQuantityToBrew())
                .price(beer.getPrice())
                .build();
        when(beerService.update(any(), any())).thenThrow(new BeerNotFoundException("Beer Not Found. UUID: " + beer.getId()));
        when(dateTimeProvider.now()).thenReturn(OFFSET_DATE_TIME);

        // when
        final var response = mockMvc.perform(put("/api/v1/beers/" + beer.getId())
                        .content(super.mapToJson(beerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        // then
        final var expectedJson = """
                    {
                       "timestamp": "2025-01-23T12:07:00Z",
                       "status": 404,
                       "errors": ["Beer Not Found. UUID: 71737f0e-11eb-4775-b8b4-ce945fdee936"]
                     }
                """;
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var expectedMap = super.mapFromJson(expectedJson, Map.class);
        final var actualMap = super.mapFromJson(responseBody, Map.class);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    void shouldThrowBadRequestWhenTryUpdateButBeerRequestIsInvalid() throws Exception {
        // given
        final var originalBeer = createBeer(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), "Test Beer", BeerStyle.ALE, "12345", 5, 10, BigDecimal.valueOf(10.99), 1, OFFSET_DATE_TIME, OFFSET_DATE_TIME);
        final var beerRequest = BeerRequest.builder()
                .build();
        when(dateTimeProvider.now()).thenReturn(OFFSET_DATE_TIME);

        // when
        final var response = mockMvc.perform(put("/api/v1/beers/" + originalBeer.getId())
                        .content(super.mapToJson(beerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        // then
        final var expectedJson = """
                    {
                       "timestamp": "2025-01-23T12:07:00Z",
                       "status": 400,
                       "errors": {
                         "beerName": ["must not be blank"],
                         "beerStyle": ["must not be null"],
                         "upc": ["must not be blank"],
                         "minOnHand": ["must not be null"],
                         "quantityToBrew": ["must not be null"],
                         "price": ["must not be null"]
                       }
                     }
                """;
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var expectedMap = super.mapFromJson(expectedJson, Map.class);
        final var actualMap = super.mapFromJson(responseBody, Map.class);
        assertEquals(expectedMap, actualMap);
        verify(beerService, never()).update(any(), any());
    }

    @Test
    void shouldDeletedBeer() throws Exception {
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
    void shouldThrowNotFoundWhenTryDeleteBeerButBeerByIdNotExists() throws Exception {
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

    private Beer createBeer(UUID id, String beerName, BeerStyle beerStyle, String upc, int minOnHand, int quantityToBrew, BigDecimal price, int version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate) {
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