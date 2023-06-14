package io.everyonecodes.project.movie_recommendations.persistance.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public Movie(String tmdbId, String title, List<Genre> genres, int releaseYear) {
        this.tmdbId = tmdbId;
        this.title = title;
        this.genres = genres;
        this.releaseYear = releaseYear;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return releaseYear == movie.releaseYear && Objects.equals(id, movie.id) && Objects.equals(tmdbId, movie.tmdbId) && Objects.equals(title, movie.title) && Objects.equals(genres, movie.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tmdbId, title, genres, releaseYear);
    }
}