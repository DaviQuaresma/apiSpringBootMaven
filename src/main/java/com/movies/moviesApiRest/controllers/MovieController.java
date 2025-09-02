package com.movies.moviesApiRest.controllers;

import com.movies.moviesApiRest.dtos.MovieResponse;
import com.movies.moviesApiRest.models.Movie;
import com.movies.moviesApiRest.models.PaginationRequest;
import com.movies.moviesApiRest.responses.PagingResult;
import com.movies.moviesApiRest.services.MovieApiExternal;
import com.movies.moviesApiRest.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping("/movie")
@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final MovieApiExternal movieApiExternal;
    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    /*@GetMapping
    public List<Movie> findAllMovies() {
        return movieService.findAll();
    }*/

    // Paginação
    @GetMapping
    public ResponseEntity<PagingResult<MovieResponse>> findAllMovies(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String sortField,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction
    ) {
        final PaginationRequest request = new PaginationRequest(page, size, sortField, direction);
        final PagingResult<MovieResponse> movies = movieService.findAllPaginated(request);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public Movie findMovie(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @GetMapping("/search")
    public Mono<Movie> getMovie(@RequestParam String title) {
        log.info("title: {}", title);

        return movieApiExternal.getDataUnique(title)
                .flatMap(response -> {
                    Movie movie = new Movie();
                    movie.setTitle(response.getTitle());
                    movie.setYear(response.getYear());
                    movie.setRuntime(response.getRuntime());
                    movie.setGenre(response.getGenre());
                    movie.setDirector(response.getDirector());
                    movie.setActors(response.getActors());
                    movie.setPlot(response.getPlot());
                    movie.setLanguage(response.getLanguage());
                    movie.setCountry(response.getCountry());
                    movie.setAwards(response.getAwards());
                    movie.setPoster(response.getPoster());
                    movie.setImdbRating(response.getImdbRating());
                    movie.setType(response.getType());
                    movie.setBoxOffice(response.getBoxOffice());

                    return movieService.saveReactive(movie);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            movieService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
