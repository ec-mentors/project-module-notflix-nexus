package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.UserEntity;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.UserEntityRepository;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserEntityServiceTest {

    @Autowired
    UserEntityService userEntityService;

    @MockBean
    UserEntityRepository userEntityRepository;

    @MockBean
    PasswordEncoder encoder;

    @MockBean
    WatchListRepository watchListRepository;

    @MockBean
    MovieService movieService;

    private final String username = "testuser";
    private final String password = "password";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() {
        userEntityService.getAllUsers();
        verify(userEntityRepository).findAll();

    }

    @Test
    void findByName() {
        UserEntity testUser = new UserEntity((long) 12312, username, password, new HashSet<>(), new WatchList());
        Optional<UserEntity> oUser = Optional.of(testUser);

        when(userEntityRepository.findByUsername(username)).thenReturn(oUser);

        var result = userEntityService.findByName(username);

        assertEquals(oUser, result);
        verify(userEntityRepository).findByUsername(username);
    }

    @Test
    public void testExistsByUsernameIfExists() {
        when(userEntityRepository.existsByUsername(username)).thenReturn(true);

        boolean result = userEntityService.existsByUsername(username);

        assertTrue(result);
        verify(userEntityRepository).existsByUsername(username);
    }

    @Test
    public void testExistsByUsernameIfDoesNotExist() {
        when(userEntityRepository.existsByUsername(username)).thenReturn(false);

        boolean result = userEntityService.existsByUsername(username);

        assertFalse(result);
        verify(userEntityRepository).existsByUsername(username);
    }

    @Test
    public void testAddUserIfDoesNotExist() {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);

        when(userEntityRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(encoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(watchListRepository.save(any(WatchList.class))).thenReturn(new WatchList());
        when(userEntityRepository.save(user)).thenReturn(user);

        UserEntity result = userEntityService.addUser(user);

        assertEquals(user, result);
        verify(userEntityRepository).findByUsername(user.getUsername());
        verify(encoder).encode(user.getPassword());
        verify(watchListRepository).save(any(WatchList.class));
        verify(userEntityRepository).save(user);
    }


    @Test
    public void testGetWatchListByUsernameIfExists() {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        WatchList watchList = new WatchList();
        user.setWatchList(watchList);
        Optional<UserEntity> optionalUser = Optional.of(user);

        when(userEntityRepository.findByUsername(username)).thenReturn(optionalUser);

        Optional<WatchList> result = userEntityService.getWatchListByUsername(username);

        assertEquals(Optional.of(watchList), result);
        verify(userEntityRepository).findByUsername(username);
    }

    @Test
    public void testGetWatchListByUsernameIfDoesNotExist() {
        Optional<UserEntity> optionalUser = Optional.empty();

        when(userEntityRepository.findByUsername(username)).thenReturn(optionalUser);

        Optional<WatchList> result = userEntityService.getWatchListByUsername(username);

        assertEquals(Optional.empty(), result);
        verify(userEntityRepository).findByUsername(username);
    }

    @Test
    public void testAddToWatchListByUsernameWhenUserExistsAndMovieNotInWatchList() {
        Movie movie = new Movie();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        WatchList watchList = new WatchList();
        user.setWatchList(watchList);
        user.addMovieToWatchList(new Movie());

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(movieService.addMovie(movie)).thenReturn(movie);
        when(watchListRepository.save(watchList)).thenReturn(watchList);
        when(userEntityRepository.save(user)).thenReturn(user);

        Movie result = userEntityService.addToWatchListByUsername(username, movie);

        assertEquals(movie, result);
        assertEquals(2, user.getWatchList().getMovies().size());
        verify(userEntityRepository).findByUsername(username);
        verify(movieService).addMovie(movie);
        verify(watchListRepository).save(watchList);
        verify(userEntityRepository).save(user);
    }

    @Test
    public void testCreateUser() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_USER");

        userEntityService.createUser(username, password, authorities);

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthorities(authorities);
        verify(userEntityRepository).save(user);
    }
}