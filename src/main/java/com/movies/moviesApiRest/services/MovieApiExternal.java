package com.movies.moviesApiRest.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.movies.moviesApiRest.dtos.MovieResponse;

@Service
public class MovieApiExternal {

    @Value("${api.key}") // definido no application.properties
    private String apiKey;

    private final WebClient webClient;

    public MovieApiExternal(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<MovieResponse> getDataUnique(String movieName) {

        try {

            if (movieName == null) {
                throw new Exception("movieName is null");
            }

            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("t", movieName)
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(MovieResponse.class);
        } catch (Exception e) {
            return Mono.error(e);
        }

    }
}
