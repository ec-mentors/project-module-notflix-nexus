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
    private String imdbId;
    @NotBlank
    private String title;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Genre> genres = new ArrayList<>();
    private int releaseYear;

    public Movie() {}

    public Movie(String imdbId, String title, List<Genre> genres, int releaseYear) {
        this.imdbId = imdbId;
        this.title = title;
        this.genres = genres;
        this.releaseYear = releaseYear;
    }

    public Long getId() {return id;}
    public String getImdbId() {return imdbId;}
    public String getTitle() {return title;}
    public List<Genre> getGenres() {return genres;}
    public int getReleaseYear() {return releaseYear;}

    public void setId(Long id) {this.id = id;}
    public void setImdbId(String imdbId) {this.imdbId = imdbId;}
    public void setTitle(String title) {this.title = title;}
    public void setGenres(List<Genre> genres) {this.genres = genres;}
    public void setReleaseYear(int releaseYear) {this.releaseYear = releaseYear;}
}