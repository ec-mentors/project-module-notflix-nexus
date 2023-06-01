package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserEntityRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final WatchListRepository watchListRepository;
    private final MovieService movieService;
    private final PasswordEncoder encoder;
    private final String userRole;

    public UserEntityService(UserEntityRepository userEntityRepository, WatchListRepository watchListRepository, MovieService movieService, PasswordEncoder encoder, @Value("${authentication.roles.user}") String userRole) {
        this.userEntityRepository = userEntityRepository;
        this.watchListRepository = watchListRepository;
        this.movieService = movieService;
        this.encoder = encoder;
        this.userRole = userRole;
    }

    public List<UserEntity> getAllUsers() {
        return userEntityRepository.findAll();
    }

    public Optional<UserEntity> findByName(String username) {
        if (username == null) {
            throw new UsernameNotFoundException("Please enter a valid username");
        }
        return userEntityRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userEntityRepository.existsByUsername(username);
    }

    public UserEntity addUser(UserEntity user) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsername(user.getUsername());
        //TODO: everyone happy with returning unchanged user if username already exists? (will not happen because of validation..?)
        if (optionalUser.isEmpty()) {
            user.setAuthorities(Set.of(userRole));
            user.setPassword(encoder.encode(user.getPassword()));
            user.setWatchList(watchListRepository.save(new WatchList()));
            userEntityRepository.save(user);
        }
        return user;
    }

    public Optional<WatchList> getWatchListByUsername(String username) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsername(username);
        return optionalUser.map(UserEntity::getWatchList);
    }

    public Movie addToWatchListByUsername(String username, Movie movie) {
        Optional<UserEntity> optionalUser = userEntityRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            movie = movieService.addMovie(movie);
            UserEntity user = optionalUser.get();
            if (!user.getWatchList().getMovies().contains(movie)) {
                user.addMovieToWatchList(movie);
            }
            watchListRepository.save(user.getWatchList());
            userEntityRepository.save(user);
        }
        return movie;
    }

    public void createUser(String username, String password, Set<String> authorities) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthorities(authorities);
        userEntityRepository.save(user);
    }

}
