package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.UserService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Collection;

@Controller
public class UIController {

    private final UserService userService;

    public UIController(UserService userService) {this.userService = userService;}

//    @GetMapping("/")
//    public String viewUuid(Model model, Principal principal) {
//        model.addAttribute("user_id", userService.getUserIdByUsername(principal.getName()));
//        return "index";
//    }

    @GetMapping("/watchlist")
    @Secured("ROLE_USER")
    public String viewUsersWatchlist(Model model, Principal principal) {
        return prepareMovieCollection(model, principal.getName().concat("'s Watchlist"), userService.getWatchListByUsername(principal.getName()).get().getMovies());
    }

    private String prepareMovieCollection(Model model, String header, Collection<Movie> movies) {
        model.addAttribute("header", header);
        model.addAttribute("movies", movies);
        return "movie_collection";
    }
}
