package io.everyonecodes.project.movie_recommendations.Communication;

import io.everyonecodes.project.movie_recommendations.Domain.User;
import io.everyonecodes.project.movie_recommendations.Repo.UserRepository;
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

    public Optional<User> findByName (String username) {
        if (username == null){
            throw new UsernameNotFoundException("Please enter a valid username");
        }
        return userRepository.findByUsername(username);
    }

    public void createUser (String username, String password, Set<String> authorities, Watchedlist watchedlist){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthorities(authorities);
        user.setWatchedList(watchedlist);
        userRepository.save(user);
    }

    /*

     */

}
