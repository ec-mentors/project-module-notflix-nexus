package io.everyonecodes.project.movie_recommendations.communication.dto;

import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import io.everyonecodes.project.movie_recommendations.persistance.repository.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieTranslator {
    private final GenreRepository genreRepository;

    public MovieTranslator(GenreRepository genreRepository) {this.genreRepository = genreRepository;}

    public Movie fromDTO(MovieDto dto) {
        //TODO: if(genre.getName()==null) genre=genreRepository.findById(genre.getId();
        List<Genre> genres = dto.getGenres().stream().map(genreRepository::save).collect(Collectors.toList());
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
