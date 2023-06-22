package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.MovieService;
import io.everyonecodes.project.movie_recommendations.logic.UserService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String viewRoot() {
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

    @GetMapping("/search")
    public String searchByTitle(@RequestParam String title, Model model) {
        model.addAttribute("title_searched", title);
        return prepareMovieCollection(MovieCollection.SEARCH_TITLE, movieService.findMoviesByTitle(title), model);
    }

    @PostMapping("/watchlist/{movieId}")
    @Secured("ROLE_USER")
    public String addById(@PathVariable Long movieId, Principal principal) {
        userService.addToWatchListById(principal.getName(), movieId);
        return "redirect:/watchlist";
    }

    @GetMapping("/watchlist/{movieId}")
    @Secured("ROLE_USER")
    public String removeById(@PathVariable Long movieId, Principal principal) {
        userService.removeFromWatchList(principal.getName(), movieId);
        return "redirect:/watchlist";
    }

    @GetMapping("/watchlist")
    @Secured("ROLE_USER")
    public String viewUsersWatchlist(Principal principal, Model model) {
        return prepareMovieCollection(MovieCollection.WATCHLIST, userService.getWatchListByUsername(principal.getName()).get().getMovies(), model);
    }

    private String prepareMovieCollection(MovieCollection type, Collection<Movie> movies, Model model) {
        model.addAttribute("collection_type", type);
        model.addAttribute("movies", movies);
        return "movie_collection";
    }
}
