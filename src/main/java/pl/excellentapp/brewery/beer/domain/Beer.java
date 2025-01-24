package pl.excellentapp.brewery.beer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {

    private UUID id;
    private String beerName;
    private BeerStyleEnum beerStyle;
    private String upc;
    private Integer minOnHand;
    private Integer quantityToBrew;
    private BigDecimal price;
    private Long version;
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
}
