package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.WatchListService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/{watchListId}")
    @Secured("ROLE_ADMIN")
    Movie addMovieToWatchListById(@PathVariable Long watchListId, @Valid @RequestBody Movie movie) {
        return watchListService.addMovieById(watchListId, movie);
    }

    @DeleteMapping("/{watchListId}/delete)")
    @Secured("ROLE_ADMIN")
    void clearWatchListById(@PathVariable Long watchListId) {
        watchListService.clearWatchListById(watchListId);
    }

    @DeleteMapping("/{watchListId}/delete/{movieId}")
    @Secured("ROLE_ADMIN")
    void removeMovieByIds(@PathVariable Long watchListId, @PathVariable Long movieId) {
        watchListService.removeMovieByIds(watchListId, movieId);
    }
}
