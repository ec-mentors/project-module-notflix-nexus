package io.everyonecodes.project.movie_recommendations.communication.controller;

import io.everyonecodes.project.movie_recommendations.logic.MovieService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/web/movies")
public class MovieViewController {
    private final MovieService movieService;

    public MovieViewController(MovieService movieService) {
        this.movieService = movieService;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @RequestMapping("/{movieId}/recommendationsById")
    public String getRecommendationsById(@PathVariable String movieId, Model model) {
        var inputMovie = movieService.findMovieByTmdbId(movieId);
        List<Movie> movies = movieService.findRecommendationsById(movieId);
        model.addAttribute("movies", movies);
        model.addAttribute("inputMovie", inputMovie.get().getTitle());
        return "recommendations";
    }
}
