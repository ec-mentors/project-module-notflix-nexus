package io.everyonecodes.project.movie_recommendations.persistance.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class WatchList {
    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Movie> movies = new HashSet<>();

    public void addMovie(Movie movie) {movies.add(movie);}
    public void removeMovieById(Long id) {
        Optional<Movie> optionalMovie = movies.stream().filter(movie -> id.equals(movie.getId())).findFirst();
        optionalMovie.ifPresent(movie -> movies.remove(movie));
    }
    public Long getId() {return id;}
    public Set<Movie> getMovies() {return movies;}
    public void setId(Long id) {this.id = id;}
    public void setMovies(Set<Movie> movies) {this.movies = movies;}
}
