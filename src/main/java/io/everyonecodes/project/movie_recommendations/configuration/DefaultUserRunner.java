package io.everyonecodes.project.movie_recommendations.configuration;

import io.everyonecodes.project.movie_recommendations.communication.client.MovieApiClient;
import io.everyonecodes.project.movie_recommendations.logic.UserService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.GenreRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.MovieRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
@ConfigurationProperties("notflix")
public class DefaultUserRunner {
    private List<UserEntity> users;
    private Set<Movie> movies;

    public void setUsers(List<UserEntity> users) {this.users = users;}
    public void setMovies(Set<Movie> movies) {this.movies = movies;}

    @Bean
    ApplicationRunner createDefaultUsers(UserService userService, UserRepository userRepository, WatchListRepository watchListRepository, MovieRepository movieRepository, MovieApiClient movieApiClient, GenreRepository genreRepository, PasswordEncoder encoder) {
        return args -> {
            userRepository.deleteAll();
            watchListRepository.deleteAll();
            movieRepository.deleteAll();
            movies.forEach(movieRepository::save);
            users.forEach(user -> {
                user.setPassword(encoder.encode(user.getPassword()));
                user.setWatchList(watchListRepository.save(new WatchList()));
                userRepository.save(user);
            });
            //TODO: would be nice to get movies from client instead
            movies.forEach(movie -> userService.addToWatchListByUsername(users.get(1).getUsername(), movie));
            movieApiClient.getListOfGenres().forEach(genreRepository::save);
        };
    }
}