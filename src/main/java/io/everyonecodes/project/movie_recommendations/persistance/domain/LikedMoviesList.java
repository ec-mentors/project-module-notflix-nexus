package io.everyonecodes.project.movie_recommendations.persistance.domain;

import javax.persistence.*;
import java.util.*;

@Entity
public class LikedMoviesList {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Movie> likedMovies = new HashSet<>();

    public void addMovie(Movie movie) {
        likedMovies.add(movie);
    }
    public void removeMovieById(Long id) {
        Optional<Movie> optionalMovie = likedMovies.stream().filter(movie -> id.equals(movie.getId())).findFirst();
        optionalMovie.ifPresent(movie -> likedMovies.remove(movie));
    }

    public void clear() {
        likedMovies.clear();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(Set<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }
}
