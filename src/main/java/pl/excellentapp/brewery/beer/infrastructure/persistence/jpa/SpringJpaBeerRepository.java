package pl.excellentapp.brewery.beer.infrastructure.persistence.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface SpringJpaBeerRepository extends CrudRepository<BeerEntity, UUID> {
}
