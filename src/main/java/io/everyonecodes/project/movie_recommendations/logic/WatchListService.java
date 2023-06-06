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

    public WatchListService(MovieService movieService, WatchListRepository watchListRepository, @Value("${notflix.fail.message")
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

//    public Movie addMovieByImdbId(Long watchListId, String imdbId) {
//        Movie returnedMovie = movieService.findMovieByImdbId(imdbId);
//        changeIfPresentById(watchListId, watchList -> {
//                if (!watchList.getMovies().contains(returnedMovie)) watchList.addMovie(returnedMovie);
//        });
//        return returnedMovie;
//    }

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

//    public String addMovieByTitle(Long watchListId, String movieTitle) {
//        Optional<Movie> movie = movieService.findMovieByTitle(movieTitle);
//        if (movie.isPresent()) {
//            changeIfPresentById(watchListId, watchList -> {
//                if (!watchList.getMovies().contains(movie)) watchList.addMovie(movie.get());
//            });
//            return movieTitle;
//        } else {
//            return failMessage;
//        }
//    }

//    public void removeMovieByTitle(Long watchListId, String movieTitle) {
//        Optional<Movie> movie = movieService.findMovieByTitle(movieTitle);
//        movie.ifPresent(value -> changeIfPresentById(watchListId, watchList -> watchList.removeMovieById(value.getId())));
//    }

//    public String addMovieByTitleFromApi(Long watchlistId, String movieTitle) {
//        Optional<Movie> movie = movieService.findMovieInApiByTitle(movieTitle);
//        if (movie.isPresent()) {
//            changeIfPresentById(watchlistId, watchList -> {
//                if (!watchList.getMovies().contains(movie)) watchList.addMovie(movie.get());
//            });
//            return movieTitle;
//        } else {
//            return failMessage;
//        }
//    }


}
