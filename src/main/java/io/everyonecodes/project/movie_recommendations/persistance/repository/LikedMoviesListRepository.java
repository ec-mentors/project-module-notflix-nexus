package io.everyonecodes.project.movie_recommendations.persistance.repository;

import io.everyonecodes.project.movie_recommendations.persistance.domain.LikedMoviesList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedMoviesListRepository extends JpaRepository<LikedMoviesList, Long> {
}
