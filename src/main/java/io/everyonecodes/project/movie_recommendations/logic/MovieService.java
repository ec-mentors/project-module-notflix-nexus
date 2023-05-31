package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {this.movieRepository = movieRepository;}

    public List<Movie> findAllMovies() {return movieRepository.findAll();}

    public void changeMovie(Movie movie) {
        Optional<Movie> optionalMovie = movieRepository.findById(movie.getId());
        if(optionalMovie.isPresent()) movieRepository.save(movie);
    }

    public Movie addMovie(Movie movie) {
        Optional<Movie> optionalMovie = movieRepository.findFirstByTitleAndGenreAndReleaseYear(movie.getTitle(), movie.getGenre(), movie.getReleaseYear());

        return optionalMovie.orElseGet(() -> movieRepository.save(movie));
    }

}
