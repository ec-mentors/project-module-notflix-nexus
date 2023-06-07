package io.everyonecodes.project.movie_recommendations.communication.dto;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieTranslator {
    public Movie fromDTO(MovieDto dto) {
        List<String> genres = new ArrayList<>();
//        genres = dto.getGenres().stream().map(GenreDto::getName).collect(Collectors.toList());
        return new Movie(dto.getImdbId(), dto.getTitle(), genres, dto.getRelease_date().getYear());
    }

//    public List<Movie> fromResultPages(List<ResultPageDto> pages) {
//        List<Movie> returned = new ArrayList<>();
//        for (ResultPageDto page : pages) {
//            for (MovieDto dto : page.getResults()) {
//                returned.add(fromDTO(dto));
//            }
//        }
//        return returned;
//    }
}
