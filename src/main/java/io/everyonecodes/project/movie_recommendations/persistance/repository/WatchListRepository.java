package io.everyonecodes.project.movie_recommendations.persistance.repository;

import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {

}
