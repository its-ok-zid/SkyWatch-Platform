package com.zidtech.skywatch.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(org.springframework.web.server.ServerWebExchange exchange) {

        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> {
                    Authentication auth = ctx.getAuthentication();
                    if (auth != null && auth.isAuthenticated()) {
                        return auth.getName(); // userId
                    }
                    return "anonymous";
                })
                .defaultIfEmpty("anonymous");
    }
}
