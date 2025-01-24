package pl.excellentapp.brewery.beer.domain.exception;

public class BeerNotFoundException extends RuntimeException {

    public BeerNotFoundException(String message) {
        super(message);
    }
}
