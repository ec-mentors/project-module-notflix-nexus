package io.everyonecodes.project.movie_recommendations.Repo;

import io.everyonecodes.project.movie_recommendations.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
