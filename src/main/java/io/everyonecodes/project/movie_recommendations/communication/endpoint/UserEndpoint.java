package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.UserService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.LikedMoviesList;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserEndpoint {
    private final UserService userService;

    public UserEndpoint(UserService userService) {this.userService = userService;}

    @GetMapping
    @Secured("ROLE_ADMIN")
    List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    UserEntity postUser(@Valid @RequestBody UserEntity user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/{userId}")
    @Secured("ROLE_ADMIN")
    void deleteUserById(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
    }

    @GetMapping("/current/id")
    @Secured("ROLE_USER")
    Optional<UUID> getUserId(Principal principal) {
        return userService.getUserIdByUsername(principal.getName());
    }

    @GetMapping("/current/watchlist")
    @Secured("ROLE_USER")
    Optional<WatchList> getWatchList(Principal principal) {
        return userService.getWatchListByUsername(principal.getName());
    }

    @PostMapping("/current/watchlist")
    @Secured("ROLE_USER")
    Movie postMovieToCurrentWatchList(Principal principal, @Valid @RequestBody Movie movie) {
        return userService.addToWatchListByUsername(principal.getName(), movie);
    }

    @DeleteMapping("/current/watchlist")
    @Secured("ROLE_USER")
    void clearCurrentWatchList(Principal principal) {
        userService.clearWatchlistByUsername(principal.getName());
    }


    @DeleteMapping("/current/watchlist/{movieId}")
    @Secured("ROLE_USER")
    void deleteMovieFromCurrentWatchListById(Principal principal, @PathVariable Long movieId) {
        userService.removeFromWatchList(principal.getName(), movieId);
    }

    @GetMapping("/current/watchlist/compare/{otherUserId}")
    @Secured("ROLE_USER")
    public Set<Movie> compareMovies (Principal principal, @PathVariable UUID otherUserId) {
        return userService.compareWatchLists(principal.getName(), otherUserId);
    }

    @GetMapping("/current/watchlist/id/{tmdbId}")
    @Secured("ROLE_USER")
    String addToWatchListByTmdbId(Principal principal, @PathVariable String tmdbId) {
        return userService.addToWatchListByTmdbId(principal.getName(), tmdbId);
    }

    @GetMapping("/current/watchlist/title/{title}")
    @Secured("ROLE_USER")
    String addToWatchListByTitle(Principal principal, @PathVariable String title) {
        return userService.addToWatchListByTitle(principal.getName(), title);
    }

    @DeleteMapping("/current/watchlist/id/{tmdbId}")
    @Secured("ROLE_USER")
    void deleteFromCurrentWatchlistByTmdbId(Principal principal, @PathVariable String tmdbId) {
        userService.removeFromWatchListByTmdbId(principal.getName(), tmdbId);
    }

    @DeleteMapping("/current/watchlist/title/{title}")
    @Secured("ROLE_USER")
    void deleteFromCurrentWatchlistByTitle(Principal principal, @PathVariable String title)  {
        userService.removeFromWatchListByTitle(principal.getName(), title);
    }

    @PostMapping("/current/likedmovies")
    @Secured("ROLE_USER")
    Movie postToCurrentLikedMoviesList(Principal principal, @Valid @RequestBody Movie movie) {
        return userService.addToLikedMoviesListByUsername(principal.getName(), movie);
    }

    @DeleteMapping("/current/likedmovies/{movieId}")
    @Secured("ROLE_USER")
    void deleteMovieFromCurrentLikedMoviesListByIs(Principal principal, @PathVariable Long movieId) {
        userService.removeFromLikedMoviesList(principal.getName(), movieId);
    }

    @GetMapping("/current/likedmovies")
    @Secured("ROLE_USER")
    Optional<LikedMoviesList> getLikedMoviesList(Principal principal) {
        return userService.getLikedMoviesListByUsername(principal.getName());
    }

    @DeleteMapping("/current/likedmovies")
    @Secured("ROLE_USER")
    void clearCurrentLikedMoviesList(Principal principal) {
        userService.clearLikedMoviesListByUsername(principal.getName());
    }

    @GetMapping("/current/likedmovies/id/{tmdbId}")
    @Secured("ROLE_USER")
    String addToCurrentLikedMoviesListByImdbId(Principal principal, @PathVariable String tmdbId) {
        return userService.addToLikedMoviesListByTmdbId(principal.getName(), tmdbId);
    }

    @GetMapping("/current/likedmovies/title/{title}")
    @Secured("ROLE_USER")
    String addToCurrentLikedMoviesListByTitle(Principal principal, @PathVariable String title) {
        return userService.addToLikedMoviesListByTitle(principal.getName(), title);
    }

    @DeleteMapping("/current/likedmovies/id/{tmdbId}")
    @Secured("ROLE_USER")
    void deleteFromCurrentLikedMoviesListByImdbId(Principal principal, @PathVariable String tmdbId) {
        userService.removeFromLikedMoviesListByTmdbId(principal.getName(), tmdbId);
    }

    @DeleteMapping("/current/likedmovies/title/{title}")
    @Secured("ROLE_USER")
    void deleteFromCurrentLikedMoviesListByTitle(Principal principal, @PathVariable String title) {
        userService.removeFromLikedMoviesListByTitle(principal.getName(), title);
    }
}
