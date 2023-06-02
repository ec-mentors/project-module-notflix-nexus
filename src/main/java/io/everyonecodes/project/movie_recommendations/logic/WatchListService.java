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
