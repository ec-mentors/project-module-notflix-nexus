package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final WatchListRepository watchListRepository;
    private final MovieService movieService;
    private final PasswordEncoder encoder;
    private final String userRole;

    public UserService(UserRepository userRepository, WatchListRepository watchListRepository, MovieService movieService, PasswordEncoder encoder, @Value("${notflix.roles.user}") String userRole) {
        this.userRepository = userRepository;
        this.watchListRepository = watchListRepository;
        this.movieService = movieService;
        this.encoder = encoder;
        this.userRole = userRole;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findByName(String username) {
        if (username == null) {
            throw new UsernameNotFoundException("Please enter a valid username");
        }
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserEntity addUser(UserEntity user) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isEmpty()) {
            user.setAuthorities(Set.of(userRole));
            user.setPassword(encoder.encode(user.getPassword()));
            user.setWatchList(watchListRepository.save(new WatchList()));
            userRepository.save(user);
        }
        return user;
    }

    public Optional<WatchList> getWatchListByUsername(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(UserEntity::getWatchList);
    }

    public Movie addToWatchListByUsername(String username, Movie movie) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            movie = movieService.addMovie(movie);
            UserEntity user = optionalUser.get();
            if (!user.getWatchList().getMovies().contains(movie)) {
                user.addMovieToWatchList(movie);
            }
            watchListRepository.save(user.getWatchList());
            userRepository.save(user);
        }
        return movie;
    }
}
