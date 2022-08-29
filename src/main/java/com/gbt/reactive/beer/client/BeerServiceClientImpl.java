package com.gbt.reactive.beer.client;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gbt.reactive.beer.domain.Beer;
import com.gbt.reactive.beer.domain.GetBeersRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Service
public class BeerServiceClientImpl implements BeerServiceClient {

	private final WebClient webClient;

	@Override
	public Mono<BeerListPage> listBeers(GetBeersRequest beersRequest) {
		log.info("Getting beers");
		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/beer")
						.queryParamIfPresent("pageNumber", Optional.ofNullable(beersRequest.getPageNumber()))
						.queryParamIfPresent("pageSize", Optional.ofNullable(beersRequest.getPageSize()))
						.queryParamIfPresent("beerName", Optional.ofNullable(beersRequest.getBeerName()))
						.queryParamIfPresent("beerStyle", Optional.ofNullable(beersRequest.getBeerStyle()))
						.queryParamIfPresent("showInventoryOnHand",
								Optional.ofNullable(beersRequest.getShowInventoryOnHand()))
						.build())
				.exchangeToMono(response -> {
					log.info("After service call with status code {}", response.statusCode());
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToMono(BeerListPage.class);
					} else {
						return response.createException().flatMap(Mono::error);
					}
				});
	}

	@Override
	public Mono<Beer> getBeerById(UUID id, boolean showInventorOnHand) {
		log.info("Getting beer by {}", id.toString());
		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/beer/{id}")
						.queryParamIfPresent("showInventoryOnHand", Optional.of(showInventorOnHand))
						.build(id.toString()))
				.exchangeToMono(response -> {
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
		return webClient.get().uri(uriBuilder -> uriBuilder.path("/api/v1/beerUpc/{upc}").build(upc))
				.exchangeToMono(response -> {
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
