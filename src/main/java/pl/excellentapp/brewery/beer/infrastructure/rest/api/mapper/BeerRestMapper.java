package pl.excellentapp.brewery.beer.infrastructure.rest.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.excellentapp.brewery.beer.application.beer.BeerPage;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeerRequest;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeersResponse;
import pl.excellentapp.brewery.model.BeerDto;
import pl.excellentapp.brewery.model.BeerPagedList;

import java.util.List;

@Mapper
public interface BeerRestMapper {

    Beer map(BeerRequest beerRequest);

    BeerDto map(Beer beer);

    List<BeerDto> mapBeers(List<Beer> all);

    default BeersResponse map(List<Beer> all) {
        return BeersResponse.builder()
                .beers(mapBeers(all))
                .build();
    }

    @Mapping(target = "content", source = "beers")
    @Mapping(target = "number", source = "pageNumber")
    @Mapping(target = "size", source = "pageSize")
    @Mapping(target = "totalElements", source = "total")
    BeerPagedList map(BeerPage beerPage);
}
