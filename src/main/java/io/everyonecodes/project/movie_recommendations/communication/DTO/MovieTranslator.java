package io.everyonecodes.project.movie_recommendations.communication.DTO;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieTranslator {
    public Movie fromDTO(MovieDto dto) {
        List<String> genres = dto.getGenres().stream().map(GenreDto::getName).collect(Collectors.toList());
        return new Movie(dto.getImdb_id(), dto.getTitle(), genres, dto.getRelease_date().getYear());
    }
}
