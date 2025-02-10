package com.example.ecommerce.api_gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingOrdersFilter extends AbstractGatewayFilterFactory<LoggingOrdersFilter.Config> {

    public LoggingOrdersFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("Orders Logging Filter Pre:{}",exchange.getRequest().getURI());
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("Orders Logging filter using Post:{}",exchange.getResponse().getStatusCode());
            }));
        });

    }

    public static class Config{

    }
}
