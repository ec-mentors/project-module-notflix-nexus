package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.MovieService;
import io.everyonecodes.project.movie_recommendations.logic.UserService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@Controller
public class UIController {

    private final UserService userService;

    private final MovieService movieService;

    public UIController(UserService userService, MovieService movieService) {this.userService = userService;
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String viewRoot(Model model, Authentication authentication) {
        model.addAttribute("authenticated", authentication != null && authentication.isAuthenticated());
        return "index";
    }

    @GetMapping("/user")
    @Secured("ROLE_USER")
    public String viewHome(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "user";
    }


    @GetMapping("/movie/{movieId}")
    public String viewMovie(@PathVariable Long movieId, Model model, @RequestHeader String referer) {
        Optional<Movie> optionalMovie = movieService.findMovieById(movieId);
        if (optionalMovie.isPresent()) {
            model.addAttribute(optionalMovie.get());
            return "movie";
        } else {
            return "redirect:" + referer;
        }
    }

    @PostMapping("/watchlist/{movieId}")
    @Secured("ROLE_USER")
    public String removeById(@PathVariable Long movieId, Principal principal) {
        userService.removeFromWatchList(principal.getName(), movieId);
        return "redirect:/watchlist";
    }

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
