package com.gbt.reactive.beer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Beer {

	private String id;
	private String beerName;
	private BeerStyleEnum beerStyle;
	private String upc;
	private String price;
	private String createdDate;
	private String lastModifiedDate;
	private Integer quantityOnHand;
	private Integer version;

}
