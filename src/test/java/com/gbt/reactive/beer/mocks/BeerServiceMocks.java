package com.gbt.reactive.beer.mocks;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.github.jknack.handlebars.internal.Files;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

public class BeerServiceMocks {

	public static void setupMockListBeers(WireMockExtension wm1, Resource body) throws IOException {
		wm1.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/beer"))
				.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody(Files.read(body.getInputStream(), Charset.defaultCharset()))));
	}

	public static void setupMockGetBeerById(WireMockExtension wm1, Resource body) throws IOException {
		wm1.stubFor(WireMock.get(WireMock.urlMatching("/api/v1/beer/([a-z-0-9]*)\\?showInventoryOnHand=([a-z-0-9]*)"))
				.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody(Files.read(body.getInputStream(), Charset.defaultCharset()))));
	}

	public static void setupMockGetBeerByUpc(WireMockExtension wm1, Resource body) throws IOException {
		wm1.stubFor(WireMock.get(WireMock.urlMatching("/api/v1/beerUpc/([a-z0-9]*)"))
				.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody(Files.read(body.getInputStream(), Charset.defaultCharset()))));
	}

	public static void setupMockCreateBeer(WireMockExtension wm1, HttpStatus status ) {
		wm1.stubFor(WireMock.post(WireMock.urlEqualTo("/api/v1/beer")).willReturn(WireMock.aResponse()
				.withStatus(status.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

	}
	
	public static void setupMockUpdateBeer(WireMockExtension wm1, HttpStatus status ) {
		wm1.stubFor(WireMock.put(WireMock.urlMatching("/api/v1/beer/([a-z-0-9]*)")).willReturn(WireMock.aResponse()
				.withStatus(status.value()).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

	}

}
