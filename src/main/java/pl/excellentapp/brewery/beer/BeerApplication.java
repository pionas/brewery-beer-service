package pl.excellentapp.brewery.beer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients
public class BeerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeerApplication.class, args);
    }

}
