package com.gbt.reactive.beer.client;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.gbt.reactive.beer.domain.Beer;
import com.gbt.reactive.beer.domain.GetBeersRequest;

import reactor.core.publisher.Mono;

public interface BeerServiceClient {

	Mono<BeerListPage> listBeers(GetBeersRequest beersRequest);

	Mono<Beer> getBeerById(UUID id, boolean showInventorOnHand);

	Mono<Beer> getBeerByUPC(String upc);

	Mono<ResponseEntity<Void>> create(Beer beer);

	Mono<ResponseEntity<Void>> update(UUID id);

	Mono<ResponseEntity<Void>> delete(UUID id);

}
