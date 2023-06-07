package io.everyonecodes.project.movie_recommendations.communication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;

import java.time.LocalDate;
import java.util.List;

public class MovieDto {

    @JsonProperty("id")
    private String imdbId;
    private String title;
    private List<Genre> genres;
    private LocalDate release_date;

    public String getImdbId() {return imdbId;}
    public String getTitle() {return title;}
    public List<Genre> getGenres() {return genres;}
    public LocalDate getRelease_date() {return release_date;}

    public void setImdbId(String imdbId) {this.imdbId = imdbId;}
    public void setTitle(String title) {this.title = title;}
    public void setGenres(List<Genre> genres) {this.genres = genres;}
    public void setRelease_date(LocalDate release_date) {this.release_date = release_date;}
}
