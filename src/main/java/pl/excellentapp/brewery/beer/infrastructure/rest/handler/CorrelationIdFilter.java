package pl.excellentapp.brewery.beer.infrastructure.rest.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.excellentapp.brewery.beer.utils.CorrelationIdProvider;
import pl.excellentapp.brewery.beer.utils.HttpHeadersConstants;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class CorrelationIdFilter extends OncePerRequestFilter {

    private final CorrelationIdProvider correlationIdProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var correlationId = Optional.ofNullable(request.getHeader(HttpHeadersConstants.CORRELATION_ID))
                .orElseGet(this::generateCorrelationId);
        MDC.put(HttpHeadersConstants.CORRELATION_ID, correlationId);
        response.setHeader(HttpHeadersConstants.CORRELATION_ID, correlationId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(HttpHeadersConstants.CORRELATION_ID);
        }
    }

    private String generateCorrelationId() {
        return correlationIdProvider.getCorrelationId();
    }
}