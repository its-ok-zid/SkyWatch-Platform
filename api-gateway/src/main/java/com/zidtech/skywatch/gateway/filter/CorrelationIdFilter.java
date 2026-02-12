package com.zidtech.skywatch.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements WebFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.web.server.WebFilterChain chain) {

        String correlationId = exchange.getRequest()
                .getHeaders()
                .getFirst(CORRELATION_ID_HEADER);

        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }

        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header(CORRELATION_ID_HEADER, correlationId)
                .build();

        return chain.filter(exchange.mutate()
                .request(mutatedRequest)
                .build());
    }
}
