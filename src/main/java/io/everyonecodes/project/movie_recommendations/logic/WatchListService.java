package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.communication.client.MovieApiClient;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class WatchListService {
    private final MovieService movieService;
    private final WatchListRepository watchListRepository;
    private final String failMessage;

    public WatchListService(MovieService movieService, WatchListRepository watchListRepository, @Value("${notflix.fail.message}")
    String failMessage) {
        this.movieService = movieService;
        this.watchListRepository = watchListRepository;
        this.failMessage = failMessage;
    }

    public WatchList createNewWatchList() {
        return watchListRepository.save(new WatchList());
    }

    public List<WatchList> findAllWatchLists() {
        return watchListRepository.findAll();
    }

    public Optional<WatchList> findWatchListById(Long id) {
        return watchListRepository.findById(id);
    }

    public Movie addMovieById(Long watchListId, Movie movie) {
        Movie returnedMovie = movieService.addMovie(movie);
        changeIfPresentById(watchListId, watchList -> {
            if (!watchList.getMovies().contains(returnedMovie)) watchList.addMovie(movie);
        });
        return returnedMovie;
    }

    public String addMovieByTmdbId(Long watchListId, String tmdbId) {
        Optional<Movie> returnedMovie = movieService.findMovieByTmdbId(tmdbId);
        Optional<WatchList> watchList = watchListRepository.findById(watchListId);
        if (returnedMovie.isPresent() && watchList.isPresent()) {
            movieService.addMovie(returnedMovie.get());
            watchList.get().getMovies().add(returnedMovie.get());
            watchListRepository.save(watchList.get());
            return tmdbId;
        }
        return failMessage;
    }

    public void clearWatchListById(Long watchListId) {
        changeIfPresentById(watchListId, WatchList::clear);
    }

    public void removeMovieByIds(Long watchListId, Long movieId) {
        changeIfPresentById(watchListId, watchList -> watchList.removeMovieById(movieId));
    }

    private void changeIfPresentById(Long watchListId, Consumer<WatchList> change) {
        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);
        optionalWatchList.ifPresent(watchList -> {
            change.accept(watchList);
            watchListRepository.save(watchList);
        });
    }

    public String addMovieByTitle(Long watchListId, String movieTitle) {
        Optional<Movie> returnedMovie = movieService.findMoviesByTitle(movieTitle).stream().findFirst();
        Optional<WatchList> watchList = watchListRepository.findById(watchListId);
        if (returnedMovie.isPresent() && watchList.isPresent()) {
            movieService.addMovie(returnedMovie.get());
            watchList.get().getMovies().add(returnedMovie.get());
            watchListRepository.save(watchList.get());
            return  movieTitle;
        }
        return failMessage;
    }

    public void removeMovieByTitle(Long watchListId, String movieTitle) {
        Optional<Movie> returnedMovie = movieService.findMoviesByTitle(movieTitle).stream().findFirst();
        returnedMovie.ifPresent(value -> changeIfPresentById(watchListId, watchList -> watchList.removeMovieById(value.getId())));
    }

    public void removeMovieByTmdbId(Long watchListId, String tmdbId) {
        Optional<Movie> returnedMovie = movieService.findMovieByTmdbId(tmdbId).stream().findFirst();
        returnedMovie.ifPresent(value -> removeMovieByTitle(watchListId, value.getTitle()));
    }
}
