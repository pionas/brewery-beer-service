package pl.excellentapp.brewery.beer.infrastructure.persistence.jpa;

import org.mapstruct.Mapper;
import pl.excellentapp.brewery.beer.domain.Beer;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
interface BeerMapper {

    List<Beer> map(Iterable<BeerEntity> beerEntities);

    Beer map(BeerEntity beerEntity);

    BeerEntity map(Beer beer);

    default Timestamp map(LocalDateTime value) {
        return value == null ? null : Timestamp.valueOf(value);
    }

    default LocalDateTime map(Timestamp value) {
        return value == null ? null : value.toLocalDateTime();
    }
}
