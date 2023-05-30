package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.WatchListService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/watch_list")
public class WatchListEndpoint {
    private final WatchListService watchListService;

    public WatchListEndpoint(WatchListService watchListService) {this.watchListService = watchListService;}

    @GetMapping("/{watchListId}")
    Optional<WatchList> getWatchListById(@PathVariable Long watchListId) {
        return watchListService.getWatchListById(watchListId);
    }

    @PostMapping("/{watchListId}/postMovie")
    Movie addMovieToWatchListById(@PathVariable Long watchListId, @RequestBody Movie movie) {
        return watchListService.addMovieById(watchListId, movie);
    }

    @DeleteMapping("/{watchListId}/delete/{movieId})")
    void removeMovieByIds(@PathVariable Long watchListId, @PathVariable Long movieId) {
        watchListService.removeMovieByIds(watchListId, movieId);
    }
}
