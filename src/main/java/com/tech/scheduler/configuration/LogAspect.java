package com.tech.scheduler.configuration;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class LogAspect implements WebFilter {

	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Override
	public Mono<Void> filter( ServerWebExchange exchange, WebFilterChain chain) {
		logger.info("Request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());

		return chain.filter(exchange).doOnSuccess(aVoid -> logger.info("Response Status: {}", exchange.getResponse().getStatusCode()));
	}
}