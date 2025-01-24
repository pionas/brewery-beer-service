package pl.excellentapp.brewery.beer.infrastructure.persistence.jpa;

import org.mapstruct.Mapper;
import pl.excellentapp.brewery.beer.domain.Beer;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

@Mapper
interface BeerMapper {

    List<Beer> map(Iterable<BeerEntity> beerEntities);

    Beer map(BeerEntity beerEntity);

    BeerEntity map(Beer beer);

    default Timestamp map(OffsetDateTime value) {
        return value == null ? null : Timestamp.from(value.toInstant());
    }

    default OffsetDateTime map(Timestamp value) {
        return value == null ? null : value.toInstant()
                .atOffset(OffsetDateTime.now().getOffset());
    }
}
