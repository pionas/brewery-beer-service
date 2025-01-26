package pl.excellentapp.brewery.beer.infrastructure.rest.api.mapper;

import org.mapstruct.Mapper;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeerRequest;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeerResponse;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeersResponse;

import java.util.List;

@Mapper
public interface BeerRestMapper {

    Beer map(BeerRequest beerRequest);

    BeerResponse map(Beer beer);

    List<BeerResponse> mapBeers(List<Beer> all);

    default BeersResponse map(List<Beer> all) {
        return BeersResponse.builder()
                .beers(mapBeers(all))
                .build();
    }
}
