package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.configuration.DefaultUserRunner;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
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
    SecurityFilterChain filterChain;

    @MockBean
    DefaultUserRunner defaultUserRunner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllMovies1() {
        movieService.findAllMovies();
        verify(movieRepository).findAll();
    }

    @Test
    void findAllMovies2() {
        List<Movie> movies = List.of(new Movie("Title1", "Genre1", 2023), new Movie("Title2", "Genre2", 2022));

        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result = movieService.findAllMovies();

        assertEquals(movies, result);
        verify(movieRepository).findAll();
    }

    @ParameterizedTest
    @CsvSource({"0", "999"})
    void findMovieById(Long movieId) {
        movieService.findMovieById(movieId);
        verify(movieRepository).findById(movieId);
    }

    @Test
    void changeMovie_MovieFound() {
        Movie movie = new Movie("Title", "Genre", 2023);
        Long movieId = 0L;

        when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));

        movieService.changeMovie(movieId, movie);

        verify(movieRepository).findById(movieId);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void changeMovie_MovieNotFound() {
        Movie movie = new Movie("Title", "Genre", 2023);
        Long movieId = 0L;

        when(movieRepository.findById(movie.getId())).thenReturn(Optional.empty());

        movieService.changeMovie(movieId, movie);

        verify(movieRepository).findById(movieId);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void addMovie_MovieFound() {
        Movie movie = new Movie("Title", "Genre", 2023);

        when(movieRepository.findFirstByTitleAndGenreAndReleaseYear(movie.getTitle(), movie.getGenre(), movie.getReleaseYear())).thenReturn(Optional.of(movie));

        Movie result = movieService.addMovie(movie);

        assertEquals(movie, result);
        verify(movieRepository, never()).save(movie);
    }

    @Test
    void addMovie_MovieNotFound() {
        Movie movie = new Movie("Title", "Genre", 2023);

        when(movieRepository.findFirstByTitleAndGenreAndReleaseYear(movie.getTitle(), movie.getGenre(), movie.getReleaseYear())).thenReturn(Optional.empty());
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