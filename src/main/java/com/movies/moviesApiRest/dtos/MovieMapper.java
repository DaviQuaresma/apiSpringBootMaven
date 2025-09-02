package com.movies.moviesApiRest.dtos;

import com.movies.moviesApiRest.models.Movie;
import com.movies.moviesApiRest.responses.MovieResponse;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    // De DTO (entrada) -> Model (para salvar no banco)
    public Movie toEntity(MovieDto dto) {
        if (dto == null) return null;

        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setYear(dto.getYear());
        movie.setRuntime(dto.getRuntime());
        movie.setGenre(dto.getGenre());
        movie.setDirector(dto.getDirector());
        movie.setActors(dto.getActors());
        movie.setPlot(dto.getPlot());
        movie.setLanguage(dto.getLanguage());
        movie.setCountry(dto.getCountry());
        movie.setAwards(dto.getAwards());
        movie.setPoster(dto.getPoster());
        movie.setImdbRating(dto.getImdbRating());
        movie.setType(dto.getType());
        movie.setBoxOffice(dto.getBoxOffice());
        return movie;
    }

    // De Model -> Response (para devolver no controller)
    public MovieResponse toResponse(Movie movie) {
        if (movie == null) return null;

        MovieResponse response = new MovieResponse();
        response.setTitle(movie.getTitle());
        response.setYear(movie.getYear());
        response.setRuntime(movie.getRuntime());
        response.setGenre(movie.getGenre());
        response.setDirector(movie.getDirector());
        response.setActors(movie.getActors());
        response.setPlot(movie.getPlot());
        response.setLanguage(movie.getLanguage());
        response.setCountry(movie.getCountry());
        response.setAwards(movie.getAwards());
        response.setPoster(movie.getPoster());
        response.setImdbRating(movie.getImdbRating());
        response.setType(movie.getType());
        response.setBoxOffice(movie.getBoxOffice());

        return response;
    }
}
