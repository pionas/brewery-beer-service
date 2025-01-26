package pl.excellentapp.brewery.beer.domain.beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerRepository {

    List<Beer> findAll();

    Beer save(Beer beer);

    Optional<Beer> findById(UUID id);

    void deleteById(UUID id);
}
