package io.everyonecodes.project.movie_recommendations.configuration;

import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserEntityRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@ConfigurationProperties("authentication")
public class DefaultUserEntityRunner {
    private Set<UserEntity> users;
    public void setUsers(Set<UserEntity> users) {this.users = users;}

    @Bean
    ApplicationRunner createDefaultUsers(UserEntityRepository userEntityRepository, WatchListRepository watchListRepository, PasswordEncoder encoder) {
        return args -> {
            userEntityRepository.deleteAll();
            users.forEach(user -> {
                user.setPassword(encoder.encode(user.getPassword()));
                user.setWatchList(watchListRepository.save(new WatchList()));
                userEntityRepository.save(user);
            });
        };
    }
}