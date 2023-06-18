package io.everyonecodes.project.movie_recommendations.configuration;

import io.everyonecodes.project.movie_recommendations.communication.client.MovieApiClient;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.repository.GenreRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@ConfigurationProperties("notflix")
public class DefaultUserRunner {
    private List<UserEntity> users;

    public void setUsers(List<UserEntity> users) {this.users = users;}

    @Bean
    ApplicationRunner createDefaultUsers(UserRepository userRepository, PasswordEncoder encoder, MovieApiClient movieApiClient, GenreRepository genreRepository) {
        return args -> {
            userRepository.deleteAll();

            users.forEach(user -> {
                user.setPassword(encoder.encode(user.getPassword()));
                userRepository.save(user);
            });
            movieApiClient.getListOfGenres().forEach(genreRepository::save);
        };
    }
}