package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class WatchListService {
    private final MovieService movieService;
    private final WatchListRepository watchListRepository;

    public WatchListService(MovieService movieService, WatchListRepository watchListRepository) {
        this.movieService = movieService;
        this.watchListRepository = watchListRepository;
    }

    public WatchList createNewWatchList() {return watchListRepository.save(new WatchList());}

    public List<WatchList> findAllWatchLists() {
        return watchListRepository.findAll();
    }

    public Optional<WatchList> findWatchListById(Long id) {
        return watchListRepository.findById(id);
    }

    public Movie addMovieById(Long watchListId, Movie movie) {
        movieService.addMovie(movie);
        ifPresentById(watchListId, watchList -> watchList.addMovie(movie));
        return movie;
    }

    public void clearWatchListById(Long watchListId) {
        ifPresentById(watchListId, WatchList::clear);
    }

    public void removeMovieByIds(Long watchListId, Long movieId) {
        ifPresentById(watchListId, watchList -> watchList.removeMovieById(movieId));
    }

    private void ifPresentById(Long watchListId, Consumer<WatchList> action) {
        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);
        optionalWatchList.ifPresent(watchList -> {
            action.accept(watchList);
            watchListRepository.save(watchList);
        });
    }
}
