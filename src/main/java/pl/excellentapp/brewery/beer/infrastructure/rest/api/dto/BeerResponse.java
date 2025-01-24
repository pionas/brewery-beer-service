package pl.excellentapp.brewery.beer.infrastructure.rest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.excellentapp.brewery.beer.domain.BeerStyleEnum;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeerResponse {

    private UUID id;
    private String beerName;
    private BeerStyleEnum beerStyle;
    private String upc;
    private Integer minOnHand;
    private Integer quantityToBrew;
    private BigDecimal price;
    private Long version;
    private OffsetDateTime createdDate;
}
