package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.UserEntityService;
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
public class UserEntityEndpoint {
    private final UserEntityService userEntityService;

    public UserEntityEndpoint(UserEntityService userEntityService) {this.userEntityService = userEntityService;}

    @PostMapping
    UserEntity postUser(@Valid @RequestBody UserEntity user) {
        return userEntityService.addUser(user);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    List<UserEntity> getAllUsers() {
        return userEntityService.getAllUsers();
    }

    @GetMapping("/current/watchlist")
    @Secured("ROLE_USER")
    Optional<WatchList> getWatchList(Principal principal) {
        return userEntityService.getWatchListByUsername(principal.getName());
    }

    @PostMapping("/current/watchlist")
    @Secured("ROLE_USER")
    Movie postMovieToCurrentWatchList(Principal principal, @Valid @RequestBody Movie movie) {
        return userEntityService.addToWatchListByUsername(principal.getName(), movie);
    }

}
