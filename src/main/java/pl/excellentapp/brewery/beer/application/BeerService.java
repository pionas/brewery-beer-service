package pl.excellentapp.brewery.beer.application;

import pl.excellentapp.brewery.beer.domain.beer.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<Beer> findAll();

    Optional<Beer> findById(UUID id);

    Beer create(Beer beer);

    Beer update(UUID beerId, Beer beer);

    void delete(UUID beerId);
}
