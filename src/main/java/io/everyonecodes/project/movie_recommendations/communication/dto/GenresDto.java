package io.everyonecodes.project.movie_recommendations.communication.dto;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;

import java.util.List;

public class GenresDto {
    private List<Genre> genres;

    public void setGenres(List<Genre> genres) {this.genres = genres;}
    public List<Genre> getGenres() {return genres;}
}
