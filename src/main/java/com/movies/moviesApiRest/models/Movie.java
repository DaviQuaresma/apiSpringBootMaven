package com.movies.moviesApiRest.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Table(name="movies")
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private String runtime; // ex: "162 min"

    @Column(nullable = false)
    private String genre; // pode ser separado em List<String>

    @Column(nullable = false)
    private String director;

    @Column(nullable = false)
    private String actors; // pode virar List<String> também

    @Column(nullable = false, length = 2000)
    private String plot;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String country;

    private String awards;

    @Column(nullable = false)
    private String poster; // link da imagem

    private String imdbRating;

    @Column(nullable = false)
    private String type; // movie, series

    private String boxOffice;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;

}
