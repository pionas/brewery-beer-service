package pl.excellentapp.brewery.beer.infrastructure.beerInventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "inventory-failover-service", url = "${rest.gateway.service}")
interface BeerInventoryFailoverFeignClient {

    @GetMapping("/inventory-failover")
    ResponseEntity<BeerInventoryResponse> getOnHandInventory();

}
