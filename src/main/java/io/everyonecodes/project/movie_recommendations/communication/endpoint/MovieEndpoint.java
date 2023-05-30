package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.MovieService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
public class MovieEndpoint {
    private final MovieService movieService;

    public MovieEndpoint(MovieService movieService) {this.movieService = movieService;}

    @PutMapping
    void updateMovie(@RequestBody Movie movie) {
        movieService.changeMovie(movie);
    }
}
