package com.gbt.reactive.beer.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.logging.LogLevel;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
public class WebClientConfiguration {

	@Bean
	public WebClient webClient(@Value("${beer.service.base-path}") String baseUrl) {
		return WebClient.builder().baseUrl(baseUrl)
				.clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(
						"reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)))
				.build();
	}

}
