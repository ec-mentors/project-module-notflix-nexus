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

    @PostMapping
    UserEntity postUser(@Valid @RequestBody UserEntity user) {
        return userService.addUser(user);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
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

}
