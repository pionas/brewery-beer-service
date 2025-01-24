package pl.excellentapp.brewery.beer.infrastructure.persistence.jpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.excellentapp.brewery.beer.domain.Beer;
import pl.excellentapp.brewery.beer.domain.BeerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
class JpaBeerRepository implements BeerRepository {

    private final SpringJpaBeerRepository springJpaBeerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<Beer> findAll() {
        return beerMapper.map(springJpaBeerRepository.findAll());
    }

    @Override
    public Beer save(Beer beer) {
        return beerMapper.map(springJpaBeerRepository.save(beerMapper.map(beer)));
    }

    @Override
    public Optional<Beer> findById(UUID id) {
        return springJpaBeerRepository.findById(id)
                .map(beerMapper::map);
    }

    @Override
    public void deleteById(UUID id) {
        springJpaBeerRepository.deleteById(id);
    }
}
