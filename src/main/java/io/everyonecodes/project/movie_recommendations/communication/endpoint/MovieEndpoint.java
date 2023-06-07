package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.MovieService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieEndpoint {
    private final MovieService movieService;

    public MovieEndpoint(MovieService movieService) {this.movieService = movieService;}

    @GetMapping
    List<Movie> getAllMovies() {return movieService.findAllMovies();}

    @PostMapping
    @Secured("ROLE_ADMIN")
    Movie postMovie(@Valid @RequestBody Movie movie) {return movieService.addMovie(movie);}

    @GetMapping("/{imdbId}")
    Optional<Movie> getMovieByImdbId(@PathVariable String imdbId) {
        return movieService.findMovieByImdbId(imdbId);
    }

    @GetMapping("/by_title/{title}")
    List<Movie> getMoviesByTitle(@PathVariable String title) {return movieService.findMoviesByTitle(title);}

    @PutMapping("/{movieId}")
    @Secured("ROLE_ADMIN")
    void updateMovie(@PathVariable Long movieId, @Valid @RequestBody Movie movie) {
        movieService.changeMovie(movieId, movie);
    }

    @DeleteMapping("/{movieId}")
    @Secured("ROLE_ADMIN")
    void deleteMovie(@PathVariable Long movieId) {
        movieService.deleteById(movieId);
    }
}
