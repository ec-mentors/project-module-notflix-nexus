package io.everyonecodes.project.movie_recommendations.persistance.repository;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByTitle(String title);
    List<Movie> findAllByGenre(String genre);

    Optional<Movie> findFirstByTitleAndGenreAndReleaseYear(String title, String genre, int releaseYear);
}
