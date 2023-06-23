package io.everyonecodes.project.movie_recommendations.communication.controller;

import io.everyonecodes.project.movie_recommendations.communication.endpoint.MovieCollection;
import io.everyonecodes.project.movie_recommendations.logic.MovieService;
import io.everyonecodes.project.movie_recommendations.logic.UserService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/friends")
    @Secured("ROLE_USER")
    public String viewFriends(Principal principal, Model model) {
        model.addAttribute("user_id", userService.getUserIdByUsername(principal.getName()).get());
        return "/friends";
    }

    @GetMapping("/friends/bad_id")
    @Secured("ROLE_USER")
    public String viewFriendsBadId(Principal principal, Model model) {
        model.addAttribute("user_id", userService.getUserIdByUsername(principal.getName()).get());
        model.addAttribute("id_not_found", true);
        return "/friends";
    }

    @GetMapping("/compare/watchlist")
    @Secured("ROLE_USER")
    public String compareWatchlist(@RequestParam String friendId, Principal principal, Model model) {
        UUID uuid;
        try {
            uuid = UUID.fromString(friendId);
        } catch (IllegalArgumentException illegalArgumentException) {
            model.addAttribute("id_not_found", true);
            return "redirect:/friends/bad_id";
        }
        Optional<UserEntity> optionalUser = userService.getUserById(UUID.fromString(friendId));
        if(optionalUser.isPresent()) {
            model.addAttribute("friend", optionalUser.get().getUsername());
            return prepareMovieCollection(MovieCollection.COMPARE_WATCHLIST, userService.compareWatchLists(principal.getName(), uuid), model);
        } else {
            return "redirect:/friends/bad_id";
        }
    }

    @PostMapping("/watchlist/{movieId}")
    @Secured("ROLE_USER")
    public String addToWLById(@PathVariable Long movieId, Principal principal) {
        userService.addToWatchListById(principal.getName(), movieId);
        return "redirect:/watchlist";
    }

    @GetMapping("/watchlist/{movieId}")
    @Secured("ROLE_USER")
    public String removeFromWLById(@PathVariable Long movieId, Principal principal) {
        userService.removeFromWatchList(principal.getName(), movieId);
        return "redirect:/watchlist";
    }

    @GetMapping("/watchlist")
    @Secured("ROLE_USER")
    public String viewUsersWatchlist(Principal principal, Model model) {
        return prepareMovieCollection(MovieCollection.WATCHLIST, userService.getWatchListByUsername(principal.getName()).get().getMovies(), model);
    }

    @PostMapping("/liked/{movieId}")
    @Secured("ROLE_USER")
    public String likeById(@PathVariable Long movieId, Principal principal) {
        userService.addToLikedMoviesListById(principal.getName(), movieId);
        return "redirect:/liked";
    }

    @GetMapping("/liked/{movieId}")
    @Secured("ROLE_USER")
    public String unlikeById(@PathVariable Long movieId, Principal principal) {
        userService.removeFromLikedMoviesList(principal.getName(), movieId);
        return "redirect:/liked";
    }

    @GetMapping("/liked")
    @Secured("ROLE_USER")
    public String viewUsersLikedMovies(Principal principal, Model model) {
        return prepareMovieCollection(MovieCollection.LIKED, userService.getLikedMoviesListByUsername(principal.getName()).get().getLikedMovies(), model);
    }

    @GetMapping("/{movieId}/recommendations")
    @Secured("ROLE_USER")
    public String getRecommendationsById(@PathVariable String movieId, Model model) {
        var inputMovie = movieService.findMovieByTmdbId(movieId);
        List<Movie> movies = movieService.findRecommendationsById(movieId);
        model.addAttribute("movies", movies);
        model.addAttribute("inputMovie", inputMovie.get().getTitle());
        return "recommendations";
    }

    private String prepareMovieCollection(MovieCollection type, Collection<Movie> movies, Model model) {
        model.addAttribute("collection_type", type);
        model.addAttribute("movies", movies);
        return "movie_collection";
    }
}
