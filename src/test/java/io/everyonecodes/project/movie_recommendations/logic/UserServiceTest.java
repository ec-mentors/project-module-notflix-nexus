package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.configuration.DefaultUserRunner;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder encoder;

//    @MockBean
//    WatchListRepository watchListRepository;

    @MockBean
    WatchListService watchListService;

//    @MockBean
//    MovieService movieService;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @MockBean
    DefaultUserRunner defaultUserRunner;

    private final String username = "testuser";
    private final String password = "password";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() {
        userService.getAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    public void testAddUserIfDoesNotExist() {
        UserEntity user = new UserEntity();
        user.setPassword(password);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(encoder.encode(user.getPassword())).thenReturn("encodedPassword");

        UserEntity result = userService.addUser(user);

        assertEquals(user, result);
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository).findByUsername(user.getUsername());
        verify(encoder).encode(password);
        verify(watchListService).createNewWatchList();
        verify(userRepository).save(user);
    }

    @Test
    void deleteUserByIdTest() {
        Long userId = 1L;

        userService.deleteUserById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testGetWatchListByUsernameIfExists() {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        WatchList watchList = new WatchList();
        user.setWatchList(watchList);
        Optional<UserEntity> optionalUser = Optional.of(user);

        when(userRepository.findByUsername(username)).thenReturn(optionalUser);

        Optional<WatchList> result = userService.getWatchListByUsername(username);

        assertEquals(Optional.of(watchList), result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void testGetWatchListByUsernameIfDoesNotExist() {
        Optional<UserEntity> optionalUser = Optional.empty();

        when(userRepository.findByUsername(username)).thenReturn(optionalUser);

        Optional<WatchList> result = userService.getWatchListByUsername(username);

        assertEquals(Optional.empty(), result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void testAddToWatchListByUsernameWhenUserExistsAndMovieNotInWatchList() {
        Movie movie = new Movie();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        WatchList watchList = new WatchList();
        watchList.setId(123L);
        user.setWatchList(watchList);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        userService.addToWatchListByUsername(username, movie);

        verify(userRepository).findByUsername(username);
        verify(watchListService).addMovieById(Mockito.anyLong(), Mockito.any(Movie.class));
    }
}