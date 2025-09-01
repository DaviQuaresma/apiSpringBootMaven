package com.movies.moviesApiRest.controllers;

import com.movies.moviesApiRest.models.Movie;
import com.movies.moviesApiRest.services.MovieApiExternal;
import com.movies.moviesApiRest.services.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping("/movie")
@RestController
public class MovieController {
    private final MovieService movieService;
    private final MovieApiExternal movieApiExternal;
    private static final Logger log = LoggerFactory.getLogger(MovieController.class);


    public MovieController(MovieService movieService, MovieApiExternal movieApiExternal) {
        this.movieService = movieService;
        this.movieApiExternal = movieApiExternal;
    }

    @GetMapping("/")
    public List<Movie> findAllMovies() {
        return movieService.findAll();
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
    public void delete(@PathVariable Long id){
        movieService.delete(id);
    }

}
