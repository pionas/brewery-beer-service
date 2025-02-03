package pl.excellentapp.brewery.beer.domain.beer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.excellentapp.brewery.model.BeerStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer implements Serializable {

    private UUID id;
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private int minOnHand;
    private int quantityToBrew;
    private BigDecimal price;
    private Integer version;
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;

    public void addQuantityToBrew(int stock) {
        this.quantityToBrew += stock;
    }
}
