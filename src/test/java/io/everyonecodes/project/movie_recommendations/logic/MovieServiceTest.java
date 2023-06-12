package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.communication.client.MovieApiClient;
import io.everyonecodes.project.movie_recommendations.configuration.DefaultUserRunner;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MovieServiceTest {

    @Autowired
    MovieService movieService;

    @MockBean
    MovieRepository movieRepository;

    @MockBean
    MovieApiClient movieApiClient;

    @MockBean
    SecurityFilterChain filterChain;

    @MockBean
    DefaultUserRunner defaultUserRunner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllMovies() {
        movieService.findAllMovies();
        verify(movieRepository).findAll();
    }

    @Test
    void findMovieByTmdbId_MovieFound() {
        Movie movie = new Movie();
        String tmdbId = "0";
        when(movieRepository.findByTmdbId(tmdbId)).thenReturn(Optional.of(movie));

        movieService.findMovieByTmdbId(tmdbId);

        verify(movieRepository).findByTmdbId(tmdbId);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(movieApiClient);
    }

    @Test
    void findMovieByTmdbId_MovieNotFound() {
        String tmdbId = "0";
        when(movieRepository.findByTmdbId(tmdbId)).thenReturn(Optional.empty());

        movieService.findMovieByTmdbId(tmdbId);

        verify(movieRepository).findByTmdbId(tmdbId);
        verify(movieApiClient).findByID(tmdbId);
    }

    @Test
    void findMoviesByTitle() {
        String title = "test";
        movieService.findMoviesByTitle(title);
        verify(movieApiClient).findByTitle(title);
    }

    @Test
    void changeMovie_MovieFound() {
        Movie movie = new Movie();
        Long movieId = 0L;

        when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));

        movieService.changeMovie(movieId, movie);

        verify(movieRepository).findById(movieId);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void changeMovie_MovieNotFound() {
        Movie movie = new Movie();
        Long movieId = 0L;

        when(movieRepository.findById(movie.getId())).thenReturn(Optional.empty());

        movieService.changeMovie(movieId, movie);

        verify(movieRepository).findById(movieId);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void addMovie_MovieFound() {
        Movie movie = new Movie();

        when(movieRepository.findFirstByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear())).thenReturn(Optional.of(movie));

        Movie result = movieService.addMovie(movie);

        assertEquals(movie, result);
        verify(movieRepository, never()).save(movie);
    }

    @Test
    void addMovie_MovieNotFound() {
        Movie movie = new Movie();

        when(movieRepository.findFirstByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear())).thenReturn(Optional.empty());
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie result = movieService.addMovie(movie);

        assertEquals(movie, result);
        verify(movieRepository).save(movie);
    }

    @Test
    void deleteById_MovieIdFound() {
        when(movieRepository.existsById(anyLong())).thenReturn(true);
        Long movieId = 0L;

        movieService.deleteById(movieId);

        verify(movieRepository).existsById(movieId);
        verify(movieRepository).deleteById(movieId);
    }

    @Test
    void deleteById_MovieIdNotFound() {
        when(movieRepository.existsById(anyLong())).thenReturn(false);
        Long movieId = 0L;

        movieService.deleteById(movieId);

        verify(movieRepository).existsById(movieId);
        verifyNoMoreInteractions(movieRepository);
    }
}