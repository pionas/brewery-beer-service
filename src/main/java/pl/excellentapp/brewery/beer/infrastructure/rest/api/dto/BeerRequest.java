package pl.excellentapp.brewery.beer.infrastructure.rest.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.excellentapp.brewery.model.BeerStyle;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerRequest {

    @NotBlank
    private String beerName;

    @NotNull
    private BeerStyle beerStyle;

    @NotBlank
    private String upc;

    @NotNull
    @Min(1)
    private Integer minOnHand;

    @NotNull
    @Min(1)
    private Integer quantityToBrew;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal price;

}
