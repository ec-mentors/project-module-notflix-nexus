package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class WatchListService {
    private final MovieService movieService;
    private final WatchListRepository watchListRepository;

    private final UserRepository userRepository;

    public WatchListService(UserRepository userRepository, MovieService movieService, WatchListRepository watchListRepository) {
        this.movieService = movieService;
        this.watchListRepository = watchListRepository;
        this.userRepository = userRepository;
    }

    public WatchList createNewWatchList() {return watchListRepository.save(new WatchList());}

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
}