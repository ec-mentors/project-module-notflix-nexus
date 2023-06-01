package io.everyonecodes.project.movie_recommendations.logic;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.repository.MovieRepository;
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
class MovieServiceTest {

    @Autowired
    MovieService movieService;

    @MockBean
    MovieRepository movieRepository;

    @MockBean
    SecurityFilterChain filterChain;

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

        Assertions.assertEquals(movies, result);
        verify(movieRepository).findAll();
    }

    @Test
    void changeMovie_MovieFound() {
        Movie movie = new Movie("Title", "Genre", 2023);

        when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));

        movieService.changeMovie(movie);

        verify(movieRepository).findById(movie.getId());
        verify(movieRepository).save(movie);
    }

    @Test
    void changeMovie_MovieNotFound() {
        Movie movie = new Movie("Title", "Genre", 2023);

        when(movieRepository.findById(movie.getId())).thenReturn(Optional.empty());

        movieService.changeMovie(movie);

        verify(movieRepository).findById(movie.getId());
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void addMovie_MovieFound() {
        Movie movie = new Movie("Title", "Genre", 2023);

        when(movieRepository.findFirstByTitleAndGenreAndReleaseYear(movie.getTitle(), movie.getGenre(), movie.getReleaseYear())).thenReturn(Optional.of(movie));

        Movie result = movieService.addMovie(movie);

        Assertions.assertEquals(movie, result);
        verify(movieRepository, never()).save(movie);
    }

    @Test
    void addMovie_MovieNotFound() {
        Movie movie = new Movie("Title", "Genre", 2023);

        when(movieRepository.findFirstByTitleAndGenreAndReleaseYear(movie.getTitle(), movie.getGenre(), movie.getReleaseYear())).thenReturn(Optional.empty());
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie result = movieService.addMovie(movie);

        Assertions.assertEquals(movie, result);
        verify(movieRepository).save(movie);
    }
}