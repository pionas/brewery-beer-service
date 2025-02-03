package pl.excellentapp.brewery.beer.application.beer;

import org.springframework.data.domain.PageRequest;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.model.BeerStyle;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    BeerPage list(String beerName, BeerStyle beerStyle, PageRequest pageRequest);

    Optional<Beer> findById(UUID id);

    Beer create(Beer beer);

    Beer update(UUID beerId, Beer beer);

    void delete(UUID beerId);
}
