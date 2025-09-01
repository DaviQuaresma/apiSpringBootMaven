package com.movies.moviesApiRest.repositories;


import com.movies.moviesApiRest.models.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
    Optional<Movie> findById(Long id);
    List<Movie> findAll();
}
