package pl.excellentapp.brewery.beer.infrastructure.beerInventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "inventory-service", url = "${rest.inventory.service}", fallback = BeerInventoryFailoverClient.class)
interface BeerInventoryFeignClient {

    @GetMapping("/{beerId}")
    ResponseEntity<BeerInventoryResponse> getOnHandInventory(@PathVariable("beerId") UUID beerId);

}
