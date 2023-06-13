package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.communication.client.MovieApiClient;
import io.everyonecodes.project.movie_recommendations.configuration.DefaultUserRunner;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;
import io.everyonecodes.project.movie_recommendations.persistance.domain.LikedMoviesList;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.LikedMoviesListRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class LikedMoviesListServiceTest {

    @Autowired
    LikedMoviesListService likedMoviesListService;

    @MockBean
    MovieService movieService;

    @MockBean
    LikedMoviesListRepository likedMoviesListRepository;

    @MockBean
    SecurityFilterChain filterChain;

    @MockBean
    DefaultUserRunner defaultUserRunner;

    @MockBean
    MovieApiClient movieApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewLikedMoviesList() {
        likedMoviesListService.createNewLikedMoviesList();
        verify(likedMoviesListRepository).save(any(LikedMoviesList.class));
    }

    @Test
    void findAllLikedMoviesLists() {
        likedMoviesListService.findAllLikedMoviesLists();

        verify(likedMoviesListRepository).findAll();
    }

    @Test
    void findLikedMoviesListById() {
        Long id = 123L;
        likedMoviesListService.findLikedMoviesListById(id);
        verify(likedMoviesListRepository).findById(id);
    }

    @Test
    void addMovieById() {
        Long id = 123L;
        Movie movie = new Movie("1234", "Movie", List.of(new Genre()), 2023);
        LikedMoviesList likedMoviesList = new LikedMoviesList();
        when(movieService.addMovie(movie)).thenReturn(movie);
        when(likedMoviesListRepository.findById(id)).thenReturn(Optional.of(likedMoviesList));

        Movie result = likedMoviesListService.addMovieById(id, movie);

        Assertions.assertEquals(movie, result);
        verify(movieService).addMovie(movie);
        verify(likedMoviesListRepository).findById(id);
        verify(likedMoviesListRepository).save(likedMoviesList);
    }

    @Test
    void clearLikedMoviesListById_IdFound() {
        Long likedMoviesListId = 123L;
        LikedMoviesList likedMoviesList = new LikedMoviesList();
        likedMoviesList.addMovie(new Movie());

        when(likedMoviesListRepository.findById(likedMoviesListId)).thenReturn(Optional.of(likedMoviesList));
        Assertions.assertFalse(likedMoviesList.getLikedMovies().isEmpty());

        likedMoviesListService.clearLikedMoviesListById(likedMoviesListId);

        Assertions.assertTrue(likedMoviesList.getLikedMovies().isEmpty());
        verify(likedMoviesListRepository).findById(likedMoviesListId);
        verify(likedMoviesListRepository).save(likedMoviesList);
    }

    @Test
    void clearLikedMoviesListById_IdNotFound() {
        Long likedMoviesListId = 123L;
        when(likedMoviesListRepository.findById(likedMoviesListId)).thenReturn(Optional.empty());

        likedMoviesListService.clearLikedMoviesListById(likedMoviesListId);

        verify(likedMoviesListRepository).findById(likedMoviesListId);
        verifyNoMoreInteractions(likedMoviesListRepository);
    }

    @Test
    void removeMovieByIds_IdFound() {
        Long likedMoviesListId = 123L;
        Long movieId = 456L;
        Movie movie = new Movie();
        movie.setId(movieId);
        LikedMoviesList likedMoviesList = new LikedMoviesList();
        likedMoviesList.addMovie(movie);

        when(likedMoviesListRepository.findById(likedMoviesListId)).thenReturn(Optional.of(likedMoviesList));
        Assertions.assertTrue(likedMoviesList.getLikedMovies().contains(movie));

        likedMoviesListService.removeMovieByIds(likedMoviesListId, movieId);

        Assertions.assertFalse(likedMoviesList.getLikedMovies().contains(movie));
        verify(likedMoviesListRepository).findById(likedMoviesListId);
        verify(likedMoviesListRepository).save(likedMoviesList);
    }

    @Test
    void removeMovieByIds_IdNotFound() {
        Long likedMoviesListId = 123L;
        Long movieId = 456L;

        when(likedMoviesListRepository.findById(anyLong())).thenReturn(Optional.empty());

        likedMoviesListService.removeMovieByIds(likedMoviesListId, movieId);

        verify(likedMoviesListRepository).findById(likedMoviesListId);
        verifyNoMoreInteractions(likedMoviesListRepository);
    }
}