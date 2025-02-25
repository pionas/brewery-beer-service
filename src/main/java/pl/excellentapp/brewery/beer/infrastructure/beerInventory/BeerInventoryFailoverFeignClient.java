package pl.excellentapp.brewery.beer.infrastructure.beerInventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pl.excellentapp.brewery.beer.infrastructure.configuration.FeignCorrelationIdConfiguration;

@FeignClient(name = "inventory-failover-service", url = "${rest.gateway.service}", configuration = FeignCorrelationIdConfiguration.class)
interface BeerInventoryFailoverFeignClient {

    @GetMapping("/inventory-failover")
    ResponseEntity<BeerInventoryResponse> getOnHandInventory();

}
