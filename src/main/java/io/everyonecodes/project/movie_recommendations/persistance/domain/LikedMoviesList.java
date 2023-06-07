package io.everyonecodes.project.movie_recommendations.persistance.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class LikedMoviesList {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Movie> likedMovies = new ArrayList<>();

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

    public List<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(List<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }
}
