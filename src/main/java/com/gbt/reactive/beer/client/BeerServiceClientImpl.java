package com.gbt.reactive.beer.client;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gbt.reactive.beer.domain.Beer;
import com.gbt.reactive.beer.domain.GetBeersRequest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BeerServiceClientImpl implements BeerServiceClient {

	private final WebClient webClient;
	private final String baseUrl;

	@Autowired
	public BeerServiceClientImpl(@Value("${beer.service.base-path}") String baseUrl) {
		super();
		this.baseUrl = baseUrl;
		this.webClient = WebClient.builder().baseUrl(this.baseUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

	}

	@Override
	public Mono<BeerListPage> listBeers(GetBeersRequest beersRequest) {
		log.info("Getting beers");
		return webClient.get().uri("/api/v1/beer").exchangeToMono(response -> {
			log.info("After service call with status code {}", response.statusCode());
			if (response.statusCode().equals(HttpStatus.OK)) {
				return response.bodyToMono(BeerListPage.class);
			} else {
				return response.createException().flatMap(Mono::error);
			}
		});
	}

	@Override
	public Mono<Beer> getBeerById(UUID id) {
		log.info("Getting beer by {}", id.toString());
		return webClient.get().uri(String.format("/api/v1/beer/%s", id.toString())).exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK)) {
				return response.bodyToMono(Beer.class);
			} else {
				return response.createException().flatMap(Mono::error);
			}
		});
	}

	@Override
	public Mono<Beer> getBeerByUPC(String upc) {
		log.info("Getting beer upc {}", upc);
		return webClient.get().uri(String.format("/api/v1/beerUpc/%s", upc)).exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.OK)) {
				return response.bodyToMono(Beer.class);
			} else {
				return response.createException().flatMap(Mono::error);
			}
		});
	}

	@Override
	public Mono<ResponseEntity<Void>> create(Beer beer) {
		log.info("Creating a beer  {}", beer);
		return webClient.post().uri("/api/v1/beer").exchangeToMono(response -> {
			if (response.statusCode().equals(HttpStatus.CREATED)) {
				return Mono.empty();
			} else {
				return response.createException().flatMap(Mono::error);
			}
		});
	}

	@Override
	public Mono<ResponseEntity<Void>> update(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<ResponseEntity<Void>> delete(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

}
