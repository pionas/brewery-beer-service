package pl.excellentapp.brewery.beer.application.beer;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.excellentapp.brewery.beer.domain.beer.Beer;
import pl.excellentapp.brewery.beer.domain.beer.BeerRepository;
import pl.excellentapp.brewery.beer.domain.exception.BeerNotFoundException;
import pl.excellentapp.brewery.beer.utils.DateTimeProvider;
import pl.excellentapp.brewery.model.BeerStyle;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public BeerPage list(String beerName, BeerStyle beerStyle, PageRequest pageRequest) {
        return BeerPage.of(beerRepository.list(beerName, beerStyle, pageRequest), pageRequest);
    }

    @Override
    public Optional<Beer> findById(UUID id) {
        return beerRepository.findById(id);
    }

    @Override
    public Beer create(Beer beer) {
        return beerRepository.save(beer);
    }

    @Override
    public Beer update(UUID beerId, Beer beer) {
        final var currentBeer = getBeerById(beerId);
        currentBeer.setBeerName(beer.getBeerName());
        currentBeer.setBeerStyle(beer.getBeerStyle());
        currentBeer.setUpc(beer.getUpc());
        currentBeer.setMinOnHand(beer.getMinOnHand());
        currentBeer.setQuantityToBrew(beer.getQuantityToBrew());
        currentBeer.setPrice(beer.getPrice());
        currentBeer.setVersion(beer.getVersion());
        currentBeer.setCreatedDate(beer.getCreatedDate());
        currentBeer.setLastModifiedDate(dateTimeProvider.now());
        return beerRepository.save(currentBeer);
    }

    @Override
    public void delete(UUID beerId) {
        getBeerById(beerId);
        beerRepository.deleteById(beerId);
    }

    private Beer getBeerById(UUID beerId) {
        return beerRepository.findById(beerId)
                .orElseThrow(() -> new BeerNotFoundException("Beer Not Found. UUID: " + beerId));
    }
}
