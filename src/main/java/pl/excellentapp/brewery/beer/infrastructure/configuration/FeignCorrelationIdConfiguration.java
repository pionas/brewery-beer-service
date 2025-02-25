package pl.excellentapp.brewery.beer.infrastructure.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.excellentapp.brewery.beer.utils.HttpHeadersConstants;

import java.util.Optional;

@Configuration(proxyBeanMethods = false)
public class FeignCorrelationIdConfiguration {

    @Bean
    RequestInterceptor correlationIdHeaderRequestInterceptor() {
        return this::putCorrelationId;
    }

    private void putCorrelationId(RequestTemplate template) {
        Optional.ofNullable(MDC.get(HttpHeadersConstants.CORRELATION_ID))
                .ifPresent(correlationId -> template.header(HttpHeadersConstants.CORRELATION_ID, correlationId));
    }
}
