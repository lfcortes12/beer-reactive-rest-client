package com.gbt.reactive.beer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBeersRequest {

	private Integer pageNumber;
	private Integer pageSize;
	private String beerName;
	private BeerStyleEnum beerStyle;
	private Boolean showInventoryOnHand;

}
