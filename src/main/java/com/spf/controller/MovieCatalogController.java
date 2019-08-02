package com.spf.controller;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.DiscoveryClient;
import com.spf.models.CatalogItem;
import com.spf.models.Movie;
import com.spf.models.Rating;
import com.spf.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	private RestTemplate restTemplate;
		
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		
		
		UserRating ratings=restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" +userId, UserRating.class);
		
		return ratings.getUserRating().stream().map(rating -> {
			Movie movie=restTemplate.getForObject("http://MOVIE-INFO-SERVICE/movies/" +rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getName(), "Great Movie", rating.getRating());
		})
		.collect(Collectors.toList());
		}
		
		//return Collections.singletonList(new CatalogItem("Transformer", "Great Movie", 4));
	}
