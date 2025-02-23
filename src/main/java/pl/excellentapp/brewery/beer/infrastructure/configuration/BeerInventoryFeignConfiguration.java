package pl.excellentapp.brewery.beer.infrastructure.configuration;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class BeerInventoryFeignConfiguration {

    @Bean
    @ConditionalOnProperty(name = "rest.inventory.username")
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(@Value("${rest.inventory.username}") String username,
                                                                   @Value("${rest.inventory.password}") String password) {
        return new BasicAuthRequestInterceptor(username, password);
    }
}
