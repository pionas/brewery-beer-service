package pl.excellentapp.brewery.beer.domain.beer;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import pl.excellentapp.brewery.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerRepository {

    List<Beer> findAll();

    Pair<List<Beer>, Integer> list(String beerName, BeerStyle beerStyle, PageRequest pageRequest);

    Beer save(Beer beer);

    Optional<Beer> findById(UUID id);

    void deleteById(UUID id);
}
