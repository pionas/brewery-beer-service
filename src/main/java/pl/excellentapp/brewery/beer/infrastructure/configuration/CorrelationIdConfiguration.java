package pl.excellentapp.brewery.beer.infrastructure.configuration;

import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.excellentapp.brewery.beer.utils.CorrelationIdProvider;
import pl.excellentapp.brewery.beer.utils.IdGeneratorProvider;

import java.util.Optional;

@Configuration
@AllArgsConstructor
public class CorrelationIdConfiguration {

    private final IdGeneratorProvider idGeneratorProvider;

    @Bean
    @ConditionalOnBean(Tracer.class)
    public CorrelationIdProvider tracerBasedCorrelationIdProvider(Tracer tracer) {
        return () -> Optional.of(tracer)
                .map(Tracer::currentTraceContext)
                .map(CurrentTraceContext::context)
                .map(TraceContext::traceId)
                .orElse(getCorrelationIdFromIdGenerator());
    }

    @Bean
    @ConditionalOnMissingBean(Tracer.class)
    public CorrelationIdProvider uuidBasedCorrelationIdProvider() {
        return this::getCorrelationIdFromIdGenerator;
    }

    private String getCorrelationIdFromIdGenerator() {
        return idGeneratorProvider.random().toString();
    }
}
