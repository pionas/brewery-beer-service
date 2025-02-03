package pl.excellentapp.brewery.beer.infrastructure.persistence.jpa;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.beer.domain.beer.BeerRepository;
import pl.excellentapp.brewery.model.BeerStyle;

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
    public Pair<List<Beer>, Integer> list(String beerName, BeerStyle beerStyle, PageRequest pageRequest) {
        Page<BeerEntity> beerPage;
        if (StringUtils.hasLength(beerName) && !ObjectUtils.isEmpty(beerStyle)) {
            beerPage = springJpaBeerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if (StringUtils.hasLength(beerName) && ObjectUtils.isEmpty(beerStyle)) {
            beerPage = springJpaBeerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (!StringUtils.hasLength(beerName) && !ObjectUtils.isEmpty(beerStyle)) {
            beerPage = springJpaBeerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = springJpaBeerRepository.findAll(pageRequest);
        }
        List<Beer> beers = beerPage.getContent().stream().map(beerMapper::map).toList();
        return Pair.of(beers, beerPage.getTotalPages());
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
