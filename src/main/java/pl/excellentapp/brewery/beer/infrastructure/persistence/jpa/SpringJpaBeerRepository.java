package pl.excellentapp.brewery.beer.infrastructure.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.excellentapp.brewery.model.BeerStyle;

import java.util.UUID;

@Repository
interface SpringJpaBeerRepository extends JpaRepository<BeerEntity, UUID> {

    Page<BeerEntity> findAllByBeerNameAndBeerStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest);

    Page<BeerEntity> findAllByBeerName(String beerName, PageRequest pageRequest);

    Page<BeerEntity> findAllByBeerStyle(BeerStyle beerStyle, PageRequest pageRequest);
}
