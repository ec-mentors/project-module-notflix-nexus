package io.everyonecodes.project.movie_recommendations.communication.dto;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.repository.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MovieTranslator {
    private final GenreRepository genreRepository;

    public MovieTranslator(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Movie fromDTO(MovieDto dto) {
        if (!dto.getGenreIds().isEmpty())
            dto.setGenres(dto.getGenreIds().stream().map(genreRepository::findById).map(Optional::orElseThrow).collect(Collectors.toList()));
        return new Movie(dto.getImdbId(), dto.getTmdbId(), dto.getTitle(), dto.getGenres(), dto.getRelease_date().getYear());
    }
}
