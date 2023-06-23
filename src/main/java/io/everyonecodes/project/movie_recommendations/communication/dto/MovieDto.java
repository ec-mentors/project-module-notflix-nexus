package io.everyonecodes.project.movie_recommendations.communication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieDto {

    @JsonProperty("id")
    private String tmdbId;
    private String title;
    @JsonProperty("poster_path")
    private String posterPath;
    private List<Genre> genres = new ArrayList<>();
    @JsonProperty("genre_ids")
    private List<Long> genreIds = new ArrayList<>();
    private LocalDate release_date;

    public String getTmdbId() {
        return tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public LocalDate getRelease_date() {
        return release_date;
    }

    public void setTmdbId(String tmdbId) {
        this.tmdbId = tmdbId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public void setRelease_date(LocalDate release_date) {
        this.release_date = release_date;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDto movieDto = (MovieDto) o;
        return Objects.equals(tmdbId, movieDto.tmdbId) && Objects.equals(title, movieDto.title) && Objects.equals(genres, movieDto.genres) && Objects.equals(genreIds, movieDto.genreIds) && Objects.equals(release_date, movieDto.release_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tmdbId, title, genres, genreIds, release_date);
    }
}
