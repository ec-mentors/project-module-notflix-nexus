package io.everyonecodes.project.movie_recommendations.communication.client;

import io.everyonecodes.project.movie_recommendations.communication.dto.*;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Keyword;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Controller
public class MovieApiClient {
    private final MovieTranslator movieTranslator;
    private final RestTemplate restTemplate;
    private final String url;
    private final String apiKey;
    private final String urlSearchByName;
    private final String urlGetByID;
    private final String urlKeywords;

    public MovieApiClient(
            MovieTranslator movieTranslator, RestTemplate restTemplate,
            @Value("${api.url}") String url,
            @Value("${api.key}") String apiKey,
            @Value("${api.searchbyname}") String urlSearchByName,
            @Value("${api.getbyid}") String urlGetByID,
            @Value("${api.keywords}") String urlKeywords) {
        this.urlKeywords = urlKeywords;
        this.movieTranslator = movieTranslator;
        this.restTemplate = restTemplate;
        this.url = url;
        this.apiKey = apiKey;
        this.urlSearchByName = urlSearchByName;
        this.urlGetByID = urlGetByID;
    }

    public List<Movie> findByTitle(String movieTitle) {
        ResultPageDto page = Objects.requireNonNull(restTemplate.getForObject(url + urlSearchByName + movieTitle + "&api_key=" + apiKey, ResultPageDto.class));
        return page.getResults().stream().map(movieTranslator::fromDTO).collect(toList());
    }

    //TODO: Change this to TMDBID which is what it actually is.
    public Optional<Movie> findByID(String imdbId) {
        //TODO: catch client exception, if movie is not found
        MovieDto dto = restTemplate.getForObject(url + urlGetByID + imdbId + "?api_key=" + apiKey, MovieDto.class);
        return dto == null ? Optional.empty() : Optional.ofNullable(movieTranslator.fromDTO(dto));
    }

    public List<Genre> getListOfGenres() {
        GenresDto genres = restTemplate.getForObject(url + "/3/genre/movie/list?language=en" + "&api_key=" + apiKey, GenresDto.class);
        return genres != null ? genres.getGenres() : Collections.emptyList();
    }

    public List<Keyword> getListOfKeywordsById(String id) {
        String requestKeyword = urlKeywords.replace("{movie_id}", id);
        String request = url + requestKeyword + "?api_key=" + apiKey;
        KeywordDto keywords = restTemplate.getForObject(request, KeywordDto.class);
        return keywords != null ? keywords.getKeywords() : Collections.emptyList();
    }
}