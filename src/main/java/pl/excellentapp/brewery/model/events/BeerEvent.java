package pl.excellentapp.brewery.model.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.excellentapp.brewery.beer.domain.beer.Beer;

import java.io.Serializable;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class BeerEvent implements Serializable {

    private Beer beer;
}
