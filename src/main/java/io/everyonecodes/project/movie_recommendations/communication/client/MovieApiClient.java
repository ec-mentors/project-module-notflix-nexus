package io.everyonecodes.project.movie_recommendations.communication.client;

import io.everyonecodes.project.movie_recommendations.communication.dto.MovieDto;
import io.everyonecodes.project.movie_recommendations.communication.dto.MovieTranslator;
import io.everyonecodes.project.movie_recommendations.communication.dto.ResultPageDto;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    public List<Movie> findByTitle(String movieTitle) {
        //TODO: GENRES are deactivated in Translator
        ResultPageDto page = Objects.requireNonNull(restTemplate.getForObject(url + urlSearchByName + movieTitle + "&api_key=" + apiKey, ResultPageDto.class));
        return page.getResults().stream().map(movieTranslator::fromDTO).collect(toList());
    }

    public Optional<Movie> findByID(String imdbId) {
        //TODO: catch client exception, if movie is not found
        MovieDto dto = restTemplate.getForObject(url + urlGetByID + imdbId + "?api_key=" + apiKey, MovieDto.class);
        return dto == null ? Optional.empty() : Optional.ofNullable(movieTranslator.fromDTO(dto));
    }

    public List<Genre> getListOfGenres() {
        System.out.println(url + "/3/genre/movie/list?language=en" + "&api_key=" + apiKey);
        Genre[] genres = restTemplate.getForObject(url + "/3/genre/movie/list?language=en" + "&api_key=" + apiKey, Genre[].class);
        return Arrays.asList(genres);
    }
}