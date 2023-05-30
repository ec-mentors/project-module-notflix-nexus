package io.everyonecodes.project.movie_recommendations.configuration;

import io.everyonecodes.project.movie_recommendations.logic.UserService;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ApplicationRunnerConfiguration implements ApplicationRunner {
    private final UserService userService;
    private final String username;
    private final String password;
    private final Set<String> authorities;
//    private final WatchList watchList;

    public ApplicationRunnerConfiguration(UserService userService,
                                          @Value("${spring.userdata.admin.username}") String username,
                                          @Value("${spring.userdata.admin.password}") String password,
                                          @Value("${spring.userdata.admin.authorities]") Set<String> authorities) {
//                                          WatchList watchList) {
        this.userService = userService;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
//        this.watchList = watchList;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        var admin = userService.findByName("admin");
        if (admin.isEmpty()) {
            userService.createUser(username, password, authorities);
        }
    }

}