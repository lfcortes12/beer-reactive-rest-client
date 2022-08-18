package com.gbt.reactive.beer.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.gbt.reactive.beer.domain.Beer;
import com.gbt.reactive.beer.domain.BeerStyleEnum;
import com.gbt.reactive.beer.domain.GetBeersRequest;
import com.gbt.reactive.beer.mocks.BeerServiceMocks;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;

import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BeerServiceClientTest {

	private static final String WIREMOCK_URL_PROPERTY = "WIREMOCK_URL";

	private static final WireMockConfiguration DYNAMIC_PORT = new WireMockConfiguration();

	@Autowired
	BeerServiceClient beerServiceClient;

	@RegisterExtension
	static WireMockExtension wm1 = WireMockExtension.newInstance()
			.options(DYNAMIC_PORT.dynamicPort().dynamicPort().dynamicHttpsPort()).build();

	@BeforeAll
	static void config() {
		WireMockRuntimeInfo wm1RuntimeInfo = wm1.getRuntimeInfo();
		System.setProperty(WIREMOCK_URL_PROPERTY, String.format("http://localhost:%s/", wm1RuntimeInfo.getHttpPort()));
	}

	@Test
	void listBeers() throws IOException {
		Resource responseResource = new ClassPathResource("listBeersResponse.json");
		BeerServiceMocks.setupMockListBeers(wm1, responseResource);
		var beersRequest = GetBeersRequest.builder().build();
		var beerListPageMono = beerServiceClient.listBeers(beersRequest);
		StepVerifier.create(beerListPageMono).assertNext(beerListPage -> {
			assertThat(beerListPage).isNotNull().returns(25, from(BeerListPage::getNumberOfElements)).returns(30,
					from(BeerListPage::getTotalElements));

		}).expectComplete().verify();
	}

	@CsvSource({ "497f6eca-6276-4993-bfeb-53cbbbba6f08" })
	@ParameterizedTest
	void getBeerById(String uuid) throws IOException {
		Resource responseResource = new ClassPathResource("getBeerByIdResponse.json");
		BeerServiceMocks.setupMockGetBeerById(wm1, responseResource);
		var beerMono = beerServiceClient.getBeerById(UUID.fromString(uuid));
		StepVerifier.create(beerMono).assertNext(beer -> {
			assertThat(beer).isNotNull().returns("Aguila", from(Beer::getBeerName)).returns(BeerStyleEnum.LAGER,
					from(Beer::getBeerStyle));

		}).expectComplete().verify();
	}

	@CsvSource({ "0583668718888" })
	@ParameterizedTest
	void getBeerByUPC(String upc) throws IOException {
		Resource responseResource = new ClassPathResource("getBeerByIdResponse.json");
		BeerServiceMocks.setupMockGetBeerByUpc(wm1, responseResource);
		var beerMono = beerServiceClient.getBeerByUPC(upc);
		StepVerifier.create(beerMono).assertNext(beer -> {
			assertThat(beer).isNotNull().returns("Aguila", from(Beer::getBeerName)).returns(BeerStyleEnum.LAGER,
					from(Beer::getBeerStyle));

		}).expectComplete().verify();
	}

	@Test
	void createNewBeer() {
		BeerServiceMocks.setupMockCreateBeer(wm1);
		var result = beerServiceClient.create(Beer.builder().beerName("Pielsen").beerStyle(BeerStyleEnum.PILSNER).price("40.7")
				.quantityOnHand(1000).upc("1232134").build());
		StepVerifier.create(result).expectComplete().verify();
	}
	
	@Test
	void createNewBeerWhenBadRequest() {
		BeerServiceMocks.setupMockCreateBeer(wm1);
		var result = beerServiceClient.create(Beer.builder().beerName("Pielsen").beerStyle(BeerStyleEnum.PILSNER).price("40.7")
				.quantityOnHand(1000).upc("1232134").build());
		StepVerifier.create(result).expectComplete().verify();
	}

}
