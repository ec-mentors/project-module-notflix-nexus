package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final WatchListService watchListService;
    private final PasswordEncoder encoder;
    private final String userRole;

    public UserService(UserRepository userRepository, WatchListService watchListService, PasswordEncoder encoder, @Value("${notflix.roles.user}") String userRole) {
        this.userRepository = userRepository;
        this.watchListService = watchListService;
        this.encoder = encoder;
        this.userRole = userRole;
    }


    public List<UserEntity> getAllUsers() {return userRepository.findAll();}

    public UserEntity addUser(UserEntity user) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isEmpty()) {
            user.setAuthorities(Set.of(userRole));
            user.setPassword(encoder.encode(user.getPassword()));
            user.setWatchList(watchListService.createNewWatchList());
            userRepository.save(user);
        }
        return user;
    }

    public void deleteUserById(UUID userId) {userRepository.deleteById(userId);}

    public Optional<WatchList> getWatchListByUsername(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(UserEntity::getWatchList);
    }

    public Movie addToWatchListByUsername(String username, Movie movie) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> watchListService.addMovieById(user.getWatchList().getId(), movie));
        return movie;
    }

    public void clearWatchlistByUsername(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> watchListService.clearWatchListById(user.getWatchList().getId()));
    }

    public void removeFromWatchList(String username, Long movieId) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> watchListService.removeMovieByIds(user.getWatchList().getId(), movieId));
    }
}
