package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.LikedMoviesList;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.repository.LikedMoviesListRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class LikedMoviesListService {
    private final MovieService movieService;
    private final LikedMoviesListRepository likedMoviesListRepository;
    private final String failMessage;

    private final UserRepository userRepository;

    public LikedMoviesListService(UserRepository userRepository, MovieService movieService, LikedMoviesListRepository likedMoviesListRepository, @Value("${notflix.fail.message") String failMessage) {
        this.movieService = movieService;
        this.likedMoviesListRepository = likedMoviesListRepository;
        this.failMessage = failMessage;
        this.userRepository = userRepository;
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

    public void removeMovieByImdbId(Long likedMoviesListId, String imdbId) {
        Optional<Movie> returnedMovie = movieService.findMovieByImdbId(imdbId);
        returnedMovie.ifPresent(movie -> changeIfPresentById(likedMoviesListId, likedMoviesList -> likedMoviesList.removeMovieById(movie.getId())));
    }

    public void removeMovieByTitle(Long likedMoviesListId, String title) {
        Optional<Movie> returnedMovie = movieService.findMoviesByTitle(title).stream().findFirst();
        returnedMovie.ifPresent(movie -> changeIfPresentById(likedMoviesListId, likedMoviesList -> likedMoviesList.removeMovieById(movie.getId())));
    }

    public String addMovieByImdbId(Long likedMoviesListId, String imdbId) {
        Optional<Movie> returnedMovie = movieService.findMovieByImdbId(imdbId);
        if (returnedMovie.isPresent()) {
            movieService.addMovie(returnedMovie.get());
            changeIfPresentById(likedMoviesListId, likedMoviesList -> {
                if (!likedMoviesList.getLikedMovies().contains(returnedMovie.get()))
                    likedMoviesList.addMovie(returnedMovie.get());
            });
            return imdbId;
        }
        return failMessage;
    }

    public String addMovieByTitle(Long likedMoviesListId, String movieTitle) {
        Optional<Movie> returnedMovie = movieService.findMoviesByTitle(movieTitle).stream().findFirst();
        if (returnedMovie.isPresent()) {
            movieService.addMovie(returnedMovie.get());
            changeIfPresentById(likedMoviesListId, likedMoviesList -> {
                if (!likedMoviesList.getLikedMovies().contains(returnedMovie.get()))
                    likedMoviesList.addMovie(returnedMovie.get());
            });
            return movieTitle;
        }
        return failMessage;
    }

    public List<Movie> compareMovies(Long yourUserId, Long otherUserId) {
        Optional<UserEntity> yourUser = userRepository.findById(yourUserId);
        Optional<UserEntity> otherUser = userRepository.findById(otherUserId);

        if (yourUser.isPresent() && otherUser.isPresent()) {
            List<Movie> yourMovies = yourUser.get().getLikedMovies().getLikedMovies();
            List<Movie> otherUserMovies = otherUser.get().getLikedMovies().getLikedMovies();

            List<Movie> commonMovies = new ArrayList<>(yourMovies);
            commonMovies.retainAll(otherUserMovies);

            return commonMovies;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, failMessage);
        }
    }
}
