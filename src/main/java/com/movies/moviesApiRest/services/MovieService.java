package com.movies.moviesApiRest.services;

import com.movies.moviesApiRest.responses.MovieResponse;
import com.movies.moviesApiRest.models.Movie;
import com.movies.moviesApiRest.models.PaginationRequest;
import com.movies.moviesApiRest.repositories.MovieRepository;
import com.movies.moviesApiRest.responses.PagingResult;
import com.movies.moviesApiRest.dtos.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    // Salva síncrono
    public Movie save(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie is null");
        }
        return movieRepository.save(movie);
    }

    // Salva reativo
    public Mono<Movie> saveReactive(Movie movie) {
        if (movie == null) {
            return Mono.error(new IllegalArgumentException("Movie is null"));
        }

        return Mono.fromCallable(() -> movieRepository.save(movie))
                .onErrorMap(e -> new RuntimeException("Error saving movie", e));
    }

    // Busca todos sem paginação
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    // Busca paginada
    public PagingResult<MovieResponse> findAllPaginated(PaginationRequest request) {
        final Pageable pageable = PagingResult.PaginationUtils.getPageable(request);
        final Page<Movie> entities = movieRepository.findAll(pageable);

        final List<MovieResponse> entitiesDto = entities.stream()
                .map(mapper::toResponse)
                .toList();

        return new PagingResult<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber(),
                entities.isEmpty()
        );
    }

    // Busca por id
    public Movie findById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    // Deleta por id
    public void delete(Long id) {
        movieRepository.deleteById(id);
    }
}
