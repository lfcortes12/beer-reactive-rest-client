package com.gbt.reactive.beer.client;

import java.util.List;

import com.gbt.reactive.beer.domain.Beer;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BeerListPage {

	private Integer totalPages;
	private Boolean last;
	private Boolean firts;
	private Integer totalElements;
	private Integer size;
	private Integer number;
	private Integer numberOfElements;
	private List<Beer> content;

}
