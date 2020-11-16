package com.yjy;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import reactor.core.publisher.Mono;

public class RequestTimeGatewayFilterFactory
		extends AbstractGatewayFilterFactory<RequestTimeGatewayFilterFactory.Config> {

	private static final Log log = LogFactory.getLog(GatewayFilter.class);
	private static final String REQUEST_START_TIME = "startTime";
	private static final String KEY = "withParams";

	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList(KEY);
	}

	public RequestTimeGatewayFilterFactory() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			exchange.getAttributes().put(REQUEST_START_TIME, System.currentTimeMillis());
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				Long startTime = exchange.getAttribute(REQUEST_START_TIME);
				if (startTime != null) {
					StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath()).append("：")
							.append(System.currentTimeMillis() - startTime).append("ms");
					if (config.isWithParams()) {
						sb.append(" params:").append(exchange.getRequest().getQueryParams());
					}
					log.info(sb.toString());
				}
			}));
		};
	}

	public static class Config {

		private boolean withParams;

		public boolean isWithParams() {
			return withParams;
		}

		public void setWithParams(boolean withParams) {
			this.withParams = withParams;
		}

	}
}
