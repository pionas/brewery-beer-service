package pl.excellentapp.brewery.beer.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGeneratorProvider {

    public UUID random() {
        return UUID.randomUUID();
    }
}
