package io.everyonecodes.project.movie_recommendations.persistance.repository;

import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
