package io.everyonecodes.project.movie_recommendations.persistance.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private String tmdbId;
    @NotBlank
    private String title;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Genre> genres = new ArrayList<>();
    private int releaseYear;

    public Movie() {}

    public Movie(String tmdbId, String title, List<Genre> genres) {
        this.tmdbId = tmdbId;
        this.title = title;
        this.genres = genres;
    }

    public Long getId() {return id;}
    public String getTmdbId() {return tmdbId;}
    public String getTitle() {return title;}
    public List<Genre> getGenres() {return genres;}
    public int getReleaseYear() {return releaseYear;}

    public void setId(Long id) {this.id = id;}
    public void setTmdbId(String tmdbId) {this.tmdbId = tmdbId;}
    public void setTitle(String title) {this.title = title;}
    public void setGenres(List<Genre> genres) {this.genres = genres;}
    public void setReleaseYear(int releaseYear) {this.releaseYear = releaseYear;}
}