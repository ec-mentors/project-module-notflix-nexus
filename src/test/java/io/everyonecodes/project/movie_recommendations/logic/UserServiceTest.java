package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @MockBean
    WatchListRepository watchListRepository;

    @MockBean
    MovieService movieService;

    @MockBean
    SecurityFilterChain securityFilterChain;

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
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(encoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(watchListRepository.save(any(WatchList.class))).thenReturn(new WatchList());
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.addUser(user);

        assertEquals(user, result);
        verify(userRepository).findByUsername(user.getUsername());
        verify(encoder).encode(user.getPassword());
        verify(watchListRepository).save(any(WatchList.class));
        verify(userRepository).save(user);
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
        user.setWatchList(watchList);
        user.addMovieToWatchList(new Movie());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(movieService.addMovie(movie)).thenReturn(movie);
        when(watchListRepository.save(watchList)).thenReturn(watchList);
        when(userRepository.save(user)).thenReturn(user);

        Movie result = userService.addToWatchListByUsername(username, movie);

        assertEquals(movie, result);
        assertEquals(2, user.getWatchList().getMovies().size());
        verify(userRepository).findByUsername(username);
        verify(movieService).addMovie(movie);
        verify(watchListRepository).save(watchList);
        verify(userRepository).save(user);
    }
}