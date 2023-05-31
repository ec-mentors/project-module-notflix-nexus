package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchListService {
    private final MovieService movieService;
    private final WatchListRepository watchListRepository;

    public WatchListService(MovieService movieService, WatchListRepository watchListRepository) {
        this.movieService = movieService;
        this.watchListRepository = watchListRepository;}

    public List<WatchList> findAllWatchLists() {
        return watchListRepository.findAll();
    }

    public Optional<WatchList> findWatchListById(Long id) {
        return watchListRepository.findById(id);
    }

    public Movie addMovieById(Long watchListId, Movie movie) {
        Movie newMovie = movieService.addMovie(movie);
        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);
        optionalWatchList.ifPresent(watchList -> {
            watchList.addMovie(newMovie);
            watchListRepository.save(watchList);
        });
        return newMovie;
    }

    public void clearWatchListById(Long watchListId) {
        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);
        if (optionalWatchList.isPresent()) {
            optionalWatchList.get().clear();
            watchListRepository.save(optionalWatchList.get());
        }
    }

    public void removeMovieByIds(Long watchListId, Long movieId) {
        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);
        if (optionalWatchList.isPresent()) {
            optionalWatchList.get().removeMovieById(movieId);
            watchListRepository.save(optionalWatchList.get());
        }
    }
}
