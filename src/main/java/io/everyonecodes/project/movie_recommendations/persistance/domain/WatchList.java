package io.everyonecodes.project.movie_recommendations.persistance.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Entity
public class WatchList {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Movie> movies = new ArrayList<>();

    public void addMovie(Movie movie) {movies.add(movie);}
    public void removeMovieById(Long id) {
        Optional<Movie> optionalMovie = movies.stream().filter(movie -> id.equals(movie.getId())).findFirst();
        optionalMovie.ifPresent(movie -> movies.remove(movie));
    }
    public void clear() {movies.clear();}
    public Long getId() {return id;}
    public List<Movie> getMovies() {return movies;}
    public void setId(Long id) {this.id = id;}
    public void setMovies(List<Movie> movies) {this.movies = movies;}
}
