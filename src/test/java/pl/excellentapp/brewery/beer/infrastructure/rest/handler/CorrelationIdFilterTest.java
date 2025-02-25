package pl.excellentapp.brewery.beer.infrastructure.rest.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import pl.excellentapp.brewery.beer.utils.CorrelationIdProvider;
import pl.excellentapp.brewery.beer.utils.HttpHeadersConstants;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CorrelationIdFilterTest {

    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final FilterChain filterChain = Mockito.mock(FilterChain.class);
    private final CorrelationIdProvider correlationIdProvider = Mockito.mock(CorrelationIdProvider.class);
    private final CorrelationIdFilter correlationIdFilter = new CorrelationIdFilter(correlationIdProvider);

    @Test
    void shouldGenerateNewCorrelationIdWhenNotPresentInRequest() throws ServletException, IOException {
        // given
        final var expectedCorrelationId = "correlation-id";
        when(correlationIdProvider.getCorrelationId()).thenReturn(expectedCorrelationId);

        // when
        correlationIdFilter.doFilterInternal(request, response, filterChain);

        // then
        final var correlationId = response.getHeader(HttpHeadersConstants.CORRELATION_ID);
        assertThat(correlationId).isEqualTo(expectedCorrelationId);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldUseExistingCorrelationIdFromRequest() throws ServletException, IOException {
        // given
        final var existingCorrelationId = "existing-correlation-id";
        request.addHeader(HttpHeadersConstants.CORRELATION_ID, existingCorrelationId);

        // when
        correlationIdFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(response.getHeader(HttpHeadersConstants.CORRELATION_ID)).isEqualTo(existingCorrelationId);
        verify(filterChain).doFilter(request, response);
        verify(correlationIdProvider, never()).getCorrelationId();
    }

    @Test
    void shouldClearMdcAfterRequest() throws ServletException, IOException {
        // given
        when(correlationIdProvider.getCorrelationId()).thenReturn("correlation-id");

        // when
        correlationIdFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(MDC.get(HttpHeadersConstants.CORRELATION_ID)).isNull();
    }
}