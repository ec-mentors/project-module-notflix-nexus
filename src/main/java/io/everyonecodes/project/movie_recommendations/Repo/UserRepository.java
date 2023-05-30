package io.everyonecodes.project.movie_recommendations.Repo;

import io.everyonecodes.project.movie_recommendations.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
