package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.LikedMoviesListService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.LikedMoviesList;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likedmovies")
public class LikedMoviesListEndpoint {

    private final LikedMoviesListService likedMoviesListService;

    public LikedMoviesListEndpoint(LikedMoviesListService likedMoviesList) {
        this.likedMoviesListService = likedMoviesList;
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    List<LikedMoviesList> getLikedMoviesList() {
        return likedMoviesListService.findAllLikedMoviesLists();
    }

    @GetMapping("/{likedMoviesListId}")
    @Secured("ROLE_ADMIN")
    Optional<LikedMoviesList> getLikedMoviesListById(@PathVariable Long likedMoviesListId) {
        return likedMoviesListService.findLikedMoviesListById(likedMoviesListId);
    }

    @PostMapping("/{likedMoviesListId}")
    @Secured("ROLE_ADMIN")
    Movie addMovieToLikedMoviesListById(@PathVariable Long likedMoviesListId, @Valid @RequestBody Movie movie) {
        return likedMoviesListService.addMovieById(likedMoviesListId, movie);
    }

    @DeleteMapping("/{likedMoviesListId}")
    @Secured("ROLE_ADMIN")
    void clearLikedMoviesListById(@PathVariable Long likedMoviesListId) {
        likedMoviesListService.clearLikedMoviesListById(likedMoviesListId);
    }

    @DeleteMapping("/{likedMoviesListId}/movies/{movieId}")
    @Secured("ROLE_ADMIN")
    void removeMovieByIds(@PathVariable Long likedMoviesListId, @PathVariable Long movieId) {
        likedMoviesListService.removeMovieByIds(likedMoviesListId, movieId);
    }
}