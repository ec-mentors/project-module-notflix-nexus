package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.domain.WatchList;
import io.everyonecodes.project.movie_recommendations.persistance.repository.WatchListRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WatchListServiceTest {

    @Autowired
    WatchListService watchListService;

    @MockBean
    MovieService movieService;

    @MockBean
    WatchListRepository watchListRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWatchListById() {
        Long id = 123L;
        WatchList watchList = new WatchList();

        when(watchListRepository.findById(id)).thenReturn(Optional.of(watchList));
        watchListService.getWatchListById(id);

        Optional<WatchList> result = watchListService.getWatchListById(id);
        verify(watchListRepository).findById(id);
    }

    @Test
    void addMovieById() {
        Long id = 123L;
        Movie movie = new Movie("Movie", "Genre", 2023);
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
    void removeMovieByIds() {
        Long watchListId = 123L;
        Long movieId = 456L;
        WatchList watchList = new WatchList();
        when(watchListRepository.findById(watchListId)).thenReturn(Optional.of(watchList));

        watchListService.removeMovieByIds(watchListId, movieId);
        verify(watchListRepository).findById(watchListId);
        verify(watchList).removeMovieById(movieId);
    }
}