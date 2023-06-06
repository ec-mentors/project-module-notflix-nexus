package io.everyonecodes.project.movie_recommendations.communication.DTO;

import java.time.LocalDate;
import java.util.List;

public class MovieDto {
    private String imdb_id;
    private String title;
    private List<GenreDto> genres;
    private LocalDate release_date;
    //TODO: something needed to receive DATE?

    public MovieDto() {}

    public MovieDto(String imdb_id, String title, List<GenreDto> genres, LocalDate release_date) {
        this.imdb_id = imdb_id;
        this.title = title;
        this.genres = genres;
        this.release_date = release_date;
    }

    public String getImdb_id() {return imdb_id;}
    public String getTitle() {return title;}
    public List<GenreDto> getGenres() {return genres;}
    public LocalDate getRelease_date() {return release_date;}

    public void setImdb_id(String imdb_id) {this.imdb_id = imdb_id;}
    public void setTitle(String title) {this.title = title;}
    public void setGenres(List<GenreDto> genres) {this.genres = genres;}
    public void setRelease_date(LocalDate release_date) {this.release_date = release_date;}
}
