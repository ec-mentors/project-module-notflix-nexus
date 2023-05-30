package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> findByName (String username) {
        if (username == null){
            throw new UsernameNotFoundException("Please enter a valid username");
        }
        return userRepository.findByUsername(username);
    }

    public void createUser (String username, String password, Set<String> authorities){
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthorities(authorities);
//        user.setWatchedList(watchlist);
        userRepository.save(user);
    }

    /*

     */

}
