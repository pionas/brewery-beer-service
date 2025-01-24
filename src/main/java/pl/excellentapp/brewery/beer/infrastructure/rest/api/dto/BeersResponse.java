package pl.excellentapp.brewery.beer.infrastructure.rest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeersResponse {

    private List<BeerResponse> beers;
}
