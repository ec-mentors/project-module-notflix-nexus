package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.UserService;
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
}
