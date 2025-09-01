package com.movies.moviesApiRest.services;

import com.movies.moviesApiRest.models.Movie;
import com.movies.moviesApiRest.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    // Método síncrono (opcional)
    public Movie save(Movie movie) {
        log.info("Saving movie {}", movie);
        if (movie == null) {
            throw new IllegalArgumentException("Movie is null");
        }
        return movieRepository.save(movie);
    }

    // Método reativo para WebFlux
    public Mono<Movie> saveReactive(Movie movie) {
        if (movie == null) {
            return Mono.error(new IllegalArgumentException("Movie is null"));
        }

        log.info("Saving movie reactively: {}", movie);
        // Usa fromCallable para transformar operação síncrona em Mono, com tratamento de erro
        return Mono.fromCallable(() -> movieRepository.save(movie))
                .onErrorMap(e -> new RuntimeException("Error saving movie", e));
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Movie findById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        movieRepository.deleteById(id);
    }
}
