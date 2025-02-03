package pl.excellentapp.brewery.beer.infrastructure.rest.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.excellentapp.brewery.beer.application.beer.BeerService;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeerRequest;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.dto.BeersResponse;
import pl.excellentapp.brewery.beer.infrastructure.rest.api.mapper.BeerRestMapper;
import pl.excellentapp.brewery.model.BeerDto;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beers")
class BeerRestController {
    
    private final BeerService beerService;
    private final BeerRestMapper beerRestMapper;

    @GetMapping({"", "/"})
    public ResponseEntity<BeersResponse> getBeers() {
        return new ResponseEntity<>(beerRestMapper.map(beerService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeer(@PathVariable("beerId") UUID beerId) {
        return beerService.findById(beerId)
                .map(beerRestMapper::map)
                .map(beerResponse -> new ResponseEntity<>(beerResponse, HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BeerDto> createBeer(@Valid @RequestBody BeerRequest beerRequest) {
        final var beerResponse = beerRestMapper.map(beerService.create(beerRestMapper.map(beerRequest)));

        return new ResponseEntity<>(beerResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable("beerId") UUID beerId, @Valid @RequestBody BeerRequest beerRequest) {
        final var beerResponse = beerRestMapper.map(beerService.update(beerId, beerRestMapper.map(beerRequest)));

        return new ResponseEntity<>(beerResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<HttpStatus> deleteBeer(@PathVariable("beerId") UUID beerId) {
        beerService.delete(beerId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
