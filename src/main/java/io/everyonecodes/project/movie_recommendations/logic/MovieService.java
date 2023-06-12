package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.communication.client.MovieApiClient;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    private final MovieApiClient movieApiClient;

    public MovieService(MovieRepository movieRepository, MovieApiClient movieApiClient) {this.movieRepository = movieRepository;
        this.movieApiClient = movieApiClient;
    }

    public List<Movie> findAllMovies() {return movieRepository.findAll();}

    public Optional<Movie> findMovieByTmdbId(String tmdbId) {
        Optional<Movie> optionalMovie = movieRepository.findByTmdbId(tmdbId);
        if(optionalMovie.isEmpty()) {
            optionalMovie = movieApiClient.findByID(tmdbId);
            optionalMovie.ifPresent(this::addMovie);
        }
        return optionalMovie;
    }

    public List<Movie> findMoviesByTitle(String title) {
        return movieApiClient.findByTitle(title).stream().map(this::addMovie).collect(Collectors.toList());
    }

    public void changeMovie(Long movieId, Movie movie) {
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        if(optionalMovie.isPresent()) {
            movie.setId(optionalMovie.get().getId());
            movieRepository.save(movie);
        }
    }

    public Movie addMovie(Movie movie) {
        Optional<Movie> optionalMovie = movieRepository.findFirstByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear());
        return optionalMovie.orElseGet(() -> movieRepository.save(movie));
    }

    public void deleteById(Long movieId) {
        if(movieRepository.existsById(movieId)) movieRepository.deleteById(movieId);
    }
}
