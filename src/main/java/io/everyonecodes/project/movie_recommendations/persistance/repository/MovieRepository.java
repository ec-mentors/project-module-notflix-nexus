package io.everyonecodes.project.movie_recommendations.persistance.repository;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findFirstByTitleAndReleaseYear(String title, int releaseYear);
}
