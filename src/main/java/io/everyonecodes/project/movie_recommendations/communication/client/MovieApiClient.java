package io.everyonecodes.project.movie_recommendations.communication.client;

import io.everyonecodes.project.movie_recommendations.communication.DTO.MovieDto;
import io.everyonecodes.project.movie_recommendations.communication.DTO.MovieTranslator;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Controller
public class MovieApiClient {
    private final MovieTranslator movieTranslator;
    private final RestTemplate restTemplate;
    private final String url;
    private final String apiKey;
    private final String urlSearchByName;
    private final String urlGetByID;

    public MovieApiClient(
            MovieTranslator movieTranslator, RestTemplate restTemplate,
            @Value("${api.url}") String url,
            @Value("${api.key}") String apiKey,
            @Value("${api.searchbyname}") String urlSearchByName,
            @Value("${api.getbyid}") String urlGetByID) {
        this.movieTranslator = movieTranslator;
        this.restTemplate = restTemplate;
        this.url = url;
        this.apiKey = apiKey;
        this.urlSearchByName = urlSearchByName;
        this.urlGetByID = urlGetByID;
    }

    public List<Movie> findByName(String movieTitle) {
        MovieDto[] dtos = restTemplate.getForObject(url + urlSearchByName + movieTitle + "&api_key=" + apiKey, MovieDto[].class);
        return Stream.of(dtos)
                .map(movieTranslator::fromDTO)
                .collect(toList());
    }

    public Optional<Movie> findByID(String imdbId) {
        //TODO: catch client exception, if movie is not found
        MovieDto dto = restTemplate.getForObject(url + urlGetByID + imdbId + "?api_key=" + apiKey, MovieDto.class);
        return dto == null ? Optional.empty() : Optional.ofNullable(movieTranslator.fromDTO(dto));
    }
}