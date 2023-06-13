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
    void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
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

    @GetMapping("/current/likedmovies/id/{imdbId}")
    @Secured("ROLE_USER")
    String addToCurrentLikedMoviesListByImdbId(Principal principal, @PathVariable String imdbId) {
        return userService.addToLikedMoviesListByImdbId(principal.getName(), imdbId);
    }

    @GetMapping("/current/likedmovies/title/{title}")
    @Secured("ROLE_USER")
    String addToCurrentLikedMoviesListByTitle(Principal principal, @PathVariable String title) {
        return userService.addToLikedMoviesListByTitle(principal.getName(), title);
    }

    @DeleteMapping("/current/likedmovies/id/{imdbId}")
    @Secured("ROLE_USER")
    void deleteFromCurrentLikedMoviesListByImdbId(Principal principal, @PathVariable String imdbId) {
        userService.removeFromLikedMoviesListByImdbId(principal.getName(), imdbId);
    }

    @DeleteMapping("/current/likedmovies/title/{title}")
    @Secured("ROLE_USER")
    void deleteFromCurrentLikedMoviesListByTitle(Principal principal, @PathVariable String title) {
        userService.removeFromLikedMoviesListByTitle(principal.getName(), title);
    }
}
