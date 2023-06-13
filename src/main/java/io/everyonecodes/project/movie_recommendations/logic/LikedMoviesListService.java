package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.LikedMoviesList;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.repository.LikedMoviesListRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class LikedMoviesListService {
    private final MovieService movieService;
    private final LikedMoviesListRepository likedMoviesListRepository;
    private final String failMessage;

    public LikedMoviesListService(MovieService movieService, LikedMoviesListRepository likedMoviesListRepository, @Value("${notflix.fail.message}") String failMessage) {
        this.movieService = movieService;
        this.likedMoviesListRepository = likedMoviesListRepository;
        this.failMessage = failMessage;
    }

    public LikedMoviesList createNewLikedMoviesList() {
        return likedMoviesListRepository.save(new LikedMoviesList());
    }

    public List<LikedMoviesList> findAllLikedMoviesLists() {
        return likedMoviesListRepository.findAll();
    }

    public Optional<LikedMoviesList> findLikedMoviesListById(Long id) {
        return likedMoviesListRepository.findById(id);
    }

    public Movie addMovieById(Long likedMoviesListId, Movie movie) {
        Movie returnedMovie = movieService.addMovie(movie);
        changeIfPresentById(likedMoviesListId, likedMoviesList -> {
            if (!likedMoviesList.getLikedMovies().contains(returnedMovie)) likedMoviesList.addMovie(movie);
        });
        return returnedMovie;
    }

    public void clearLikedMoviesListById(Long likedMoviesListId) {
        changeIfPresentById(likedMoviesListId, LikedMoviesList::clear);
    }

    public void removeMovieByIds(Long likedMoviesListId, Long movieId) {
        changeIfPresentById(likedMoviesListId, likedMoviesList -> likedMoviesList.removeMovieById(movieId));
    }

    private void changeIfPresentById(Long LikedMoviesListId, Consumer<LikedMoviesList> change) {
        Optional<LikedMoviesList> optionalLikedMoviesList = likedMoviesListRepository.findById(LikedMoviesListId);
        optionalLikedMoviesList.ifPresent(likedMoviesList -> {
            change.accept(likedMoviesList);
            likedMoviesListRepository.save(likedMoviesList);
        });
    }

    public void removeMovieByTmdbId(Long likedMoviesListId, String tmdbId) {
        Optional<Movie> returnedMovie = movieService.findMovieByTmdbId(tmdbId);
        returnedMovie.ifPresent(movie -> removeMovieByTitle(likedMoviesListId, movie.getTitle()));
    }

    public void removeMovieByTitle(Long likedMoviesListId, String title) {
        Optional<Movie> returnedMovie = movieService.findMoviesByTitle(title).stream().findFirst();
        returnedMovie.ifPresent(movie -> changeIfPresentById(likedMoviesListId, likedMoviesList -> {
            likedMoviesList.removeMovieById(movie.getId());
        }));
    }

    public String addMovieByTmdbId(Long likedMoviesListId, String tmdbId) {
        Optional<Movie> returnedMovie = movieService.findMovieByTmdbId(tmdbId);
        Optional<LikedMoviesList> likedMoviesList = likedMoviesListRepository.findById(likedMoviesListId);
        if (returnedMovie.isPresent() && likedMoviesList.isPresent()) {
            movieService.addMovie(returnedMovie.get());
            likedMoviesList.get().getLikedMovies().add(returnedMovie.get());
            likedMoviesListRepository.save(likedMoviesList.get());
            return tmdbId;
        }
        return failMessage;
    }

    public String addMovieByTitle(Long likedMoviesListId, String movieTitle) {
        Optional<Movie> returnedMovie = movieService.findMoviesByTitle(movieTitle).stream().findFirst();
        Optional<LikedMoviesList> likedMoviesList = likedMoviesListRepository.findById(likedMoviesListId);
        if (returnedMovie.isPresent() && likedMoviesList.isPresent()) {
            movieService.addMovie(returnedMovie.get());
            likedMoviesList.get().getLikedMovies().add(returnedMovie.get());
            likedMoviesListRepository.save(likedMoviesList.get());
            return movieTitle;
        }
        return failMessage;
    }
}
