package pl.excellentapp.brewery.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderValidatedEvent implements Serializable {

    private UUID orderId;
    private Set<UUID> beers = new HashSet<>();

}
