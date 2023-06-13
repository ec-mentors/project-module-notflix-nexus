package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.WatchListService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/watchlists")
public class WatchListEndpoint {
    private final WatchListService watchListService;

    public WatchListEndpoint(WatchListService watchListService) {this.watchListService = watchListService;}

    @GetMapping
    @Secured("ROLE_ADMIN")
    List<WatchList> getWatchLists() {
        return watchListService.findAllWatchLists();
    }

    @GetMapping("/{watchListId}")
    @Secured("ROLE_ADMIN")
    Optional<WatchList> getWatchListById(@PathVariable Long watchListId) {
        return watchListService.findWatchListById(watchListId);
    }

    @PostMapping("/{watchListId}")
    @Secured("ROLE_ADMIN")
    Movie addMovieToWatchListById(@PathVariable Long watchListId, @Valid @RequestBody Movie movie) {
        return watchListService.addMovieById(watchListId, movie);
    }

    @DeleteMapping("/{watchListId}")
    @Secured("ROLE_ADMIN")
    void clearWatchListById(@PathVariable Long watchListId) {
        watchListService.clearWatchListById(watchListId);
    }

    @DeleteMapping("/{watchListId}/movies/{movieId}")
    @Secured("ROLE_ADMIN")
    void removeMovieByIds(@PathVariable Long watchListId, @PathVariable Long movieId) {
        watchListService.removeMovieByIds(watchListId, movieId);
    }

    @GetMapping("/{watchListId}/id/{tmdbId}")
    @Secured("ROLE_ADMIN")
    String addMovieByImdbId(@PathVariable Long watchListId, @PathVariable String tmdbId) {
        return watchListService.addMovieByTmdbId(watchListId, tmdbId);
    }

    @GetMapping("/{watchListId}/title/{movieTitle}")
    @Secured("ROLE_ADMIN")
    String addMovieByTitle(@PathVariable Long watchListId, @PathVariable String movieTitle) {
        return watchListService.addMovieByTitle(watchListId, movieTitle);
    }

    @DeleteMapping("/{watchListId}/id/{tmdbId}")
    @Secured("ROLE_ADMIN")
    void removeMovieByImdbId(@PathVariable Long watchListId, @PathVariable String tmdbId) {
        watchListService.removeMovieByTmdbId(watchListId, tmdbId);
    }

    @DeleteMapping("/{watchListId}/title/{movieTitle}")
    @Secured("ROLE_ADMIN")
    void removeMovieByTitle(@PathVariable Long watchListId, @PathVariable String movieTitle) {
        watchListService.removeMovieByTitle(watchListId, movieTitle);
    }
}
