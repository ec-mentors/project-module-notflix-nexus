package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.configuration.DefaultUserRunner;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WatchListServiceTest {

    @Autowired
    WatchListService watchListService;

    @MockBean
    MovieService movieService;

    @MockBean
    WatchListRepository watchListRepository;

    @MockBean
    SecurityFilterChain filterChain;

    @MockBean
    DefaultUserRunner defaultUserRunner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewWatchList() {
        watchListService.createNewWatchList();

        verify(watchListRepository).save(any(WatchList.class));
    }

    @Test
    void findAllWatchLists() {
        watchListService.findAllWatchLists();

        verify(watchListRepository).findAll();
    }
    @Test
    void findWatchListById() {
        Long id = 123L;

        watchListService.findWatchListById(id);

        verify(watchListRepository).findById(id);
    }

    @Test
    void addMovieById() {
        Long id = 123L;
        Movie movie = new Movie("1234", "Movie", List.of(new Genre()), 2023);
        WatchList watchList = new WatchList();
        when(movieService.addMovie(movie)).thenReturn(movie);
        when(watchListRepository.findById(id)).thenReturn(Optional.of(watchList));

        Movie result = watchListService.addMovieById(id, movie);

        Assertions.assertEquals(movie, result);
        verify(movieService).addMovie(movie);
        verify(watchListRepository).findById(id);
        verify(watchListRepository).save(watchList);
    }

    @Test
    void clearWatchListById_WatchlistIdFound() {
        Long watchListId = 123L;
        WatchList watchList = new WatchList();
        watchList.addMovie(new Movie());

        when(watchListRepository.findById(watchListId)).thenReturn(Optional.of(watchList));
        assertFalse(watchList.getMovies().isEmpty());

        watchListService.clearWatchListById(watchListId);

        assertTrue(watchList.getMovies().isEmpty());
        verify(watchListRepository).findById(watchListId);
        verify(watchListRepository).save(watchList);
    }

    @Test
    void clearWatchListById_WatchlistIdNotFound() {
        Long watchListId = 123L;
        when(watchListRepository.findById(watchListId)).thenReturn(Optional.empty());

        watchListService.clearWatchListById(watchListId);

        verify(watchListRepository).findById(watchListId);
        verifyNoMoreInteractions(watchListRepository);
    }

    @Test
    void removeMovieByIds_WatchlistIdFound() {
        Long watchListId = 123L;
        Long movieId = 456L;
        Movie movie = new Movie();
        movie.setId(movieId);
        WatchList watchList = new WatchList();
        watchList.addMovie(movie);

        when(watchListRepository.findById(watchListId)).thenReturn(Optional.of(watchList));
        assertTrue(watchList.getMovies().contains(movie));

        watchListService.removeMovieByIds(watchListId, movieId);

        assertFalse(watchList.getMovies().contains(movie));
        verify(watchListRepository).findById(watchListId);
        verify(watchListRepository).save(watchList);
    }

    @Test
    void removeMovieByIds_WatchlistIdNotFound() {
        Long watchListId = 123L;
        Long movieId = 456L;

        when(watchListRepository.findById(anyLong())).thenReturn(Optional.empty());

        watchListService.removeMovieByIds(watchListId, movieId);

        verify(watchListRepository).findById(watchListId);
        verifyNoMoreInteractions(watchListRepository);
    }
}