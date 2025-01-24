package pl.excellentapp.brewery.beer.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Version;
import pl.excellentapp.brewery.beer.domain.BeerStyleEnum;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "beers")
public class BeerEntity {

    @Id
    @GeneratedValue
    private UUID id;
    private String beerName;

    @Enumerated(EnumType.STRING)
    private BeerStyleEnum beerStyle;
    private String upc;
    private Integer minOnHand;
    private Integer quantityToBrew;
    private BigDecimal price;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;
}
