package io.everyonecodes.project.movie_recommendations.communication.dto;

import io.everyonecodes.project.movie_recommendations.configuration.DefaultUserRunner;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;
import io.everyonecodes.project.movie_recommendations.persistance.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MovieTranslatorTest {

    @Autowired
    MovieTranslator movieTranslator;

    @MockBean
    GenreRepository genreRepository;

    @MockBean
    SecurityFilterChain filterChain;

    @MockBean
    DefaultUserRunner defaultUserRunner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fromDTO_releaseYearEmpty() {
        MovieDto dto = new MovieDto();
        dto.setTmdbId("tmdbId");
        dto.setTitle("title");
        dto.setGenres(List.of(new Genre()));

        assertDoesNotThrow(() -> movieTranslator.fromDTO(dto));

        Mockito.verifyNoInteractions(genreRepository);
    }
}