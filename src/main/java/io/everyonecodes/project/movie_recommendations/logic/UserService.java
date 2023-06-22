package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.LikedMoviesList;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final WatchListService watchListService;
    private final LikedMoviesListService likedMoviesListService;
    private final PasswordEncoder encoder;
    private final String userRole;

    public UserService(UserRepository userRepository, WatchListService watchListService, LikedMoviesListService likedMoviesListService, PasswordEncoder encoder, @Value("${notflix.roles.user}") String userRole) {
        this.userRepository = userRepository;
        this.watchListService = watchListService;
        this.likedMoviesListService = likedMoviesListService;
        this.encoder = encoder;
        this.userRole = userRole;
    }


    public List<UserEntity> getAllUsers() {return userRepository.findAll();}

    public Optional<UUID> getUserIdByUsername(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(UserEntity::getId);
    }

    public UserEntity addUser(UserEntity user) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isEmpty()) {
            user.setAuthorities(Set.of(userRole));
            user.setPassword(encoder.encode(user.getPassword()));
            user.setWatchList(watchListService.createNewWatchList());
            user.setLikedMovies(likedMoviesListService.createNewLikedMoviesList());
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

    public Set<Movie> compareWatchLists(String username, UUID otherUserId) {
        Optional<UserEntity> yourUser = userRepository.findByUsername(username);
        Optional<UserEntity> otherUser = userRepository.findById(otherUserId);

        if (yourUser.isPresent() && otherUser.isPresent()) {
            Set<Movie> yourMovies = yourUser.get().getWatchList().getMovies();
            Set<Movie> otherUserMovies = otherUser.get().getWatchList().getMovies();

            Set<Movie> commonMovies = new HashSet<>(yourMovies);
            commonMovies.retainAll(otherUserMovies);

            return commonMovies;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public Optional<LikedMoviesList> getLikedMoviesListByUsername(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(UserEntity::getLikedMovies);
    }

    public void clearLikedMoviesListByUsername(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser
                .ifPresent(user -> likedMoviesListService.clearLikedMoviesListById(user.getLikedMovies().getId()));
    }

    public String addToWatchListByTmdbId(String username, String tmdbId) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> watchListService.addMovieByTmdbId(user.getWatchList().getId(), tmdbId));
        return tmdbId;
    }

    public String addToLikedMoviesListByTmdbId(String username, String tmdbId) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> likedMoviesListService.addMovieByTmdbId(user.getLikedMovies().getId(), tmdbId));
        return tmdbId;
    }

    public String addToWatchListByTitle(String username, String title) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> watchListService.addMovieByTitle(user.getWatchList().getId(), title));
        return title;
    }

    public String addToLikedMoviesListByTitle(String username, String title) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> likedMoviesListService.addMovieByTitle(user.getLikedMovies().getId(), title));
        return title;
    }

    public void removeFromWatchListByTmdbId(String username, String tmdbId) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> watchListService.removeMovieByTmdbId(user.getWatchList().getId(), tmdbId));
    }

    public void removeFromLikedMoviesListByTmdbId(String username, String tmdbId) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> likedMoviesListService.removeMovieByTmdbId(user.getLikedMovies().getId(), tmdbId));
    }

    public void removeFromWatchListByTitle(String username, String title) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> watchListService.removeMovieByTitle(user.getWatchList().getId(), title));
    }

    public void removeFromLikedMoviesListByTitle(String username, String title) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> likedMoviesListService.removeMovieByTitle(user.getLikedMovies().getId(), title));
    }

    public Movie addToLikedMoviesListByUsername(String username, Movie movie) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> likedMoviesListService.addMovieById(user.getWatchList().getId(), movie));
        return movie;
    }

    public void removeFromLikedMoviesList(String username, Long movieId) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> likedMoviesListService.removeMovieByIds(user.getLikedMovies().getId(), movieId));
    }

    public void addToWatchListById(String username, Long movieId) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> watchListService.addMovieByIds(user.getWatchList().getId(), movieId));
    }

    public void addToLikedMoviesListById(String username, Long movieId) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> likedMoviesListService.addMovieByIds(user.getLikedMovies().getId(), movieId));
    }
}
