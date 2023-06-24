package io.everyonecodes.project.movie_recommendations.communication.client;

import io.everyonecodes.project.movie_recommendations.communication.dto.*;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Keyword;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    private final String recommendations;

    public MovieApiClient(
            MovieTranslator movieTranslator, RestTemplate restTemplate,
            @Value("${api.url}") String url,
            @Value("${api.key}") String apiKey,
            @Value("${api.searchbyname}") String urlSearchByName,
            @Value("${api.getbyid}") String urlGetByID,
            @Value("${api.keywords}") String urlKeywords,
            @Value("${api.recommendations}") String recommendations) {
        this.urlKeywords = urlKeywords;
        this.movieTranslator = movieTranslator;
        this.restTemplate = restTemplate;
        this.url = url;
        this.apiKey = apiKey;
        this.urlSearchByName = urlSearchByName;
        this.urlGetByID = urlGetByID;
        this.recommendations = recommendations;
    }

    public List<Movie> findByTitle(String movieTitle) {
        ResultPageDto page = Objects.requireNonNull(restTemplate.getForObject(url + urlSearchByName + movieTitle + "&api_key=" + apiKey, ResultPageDto.class));
        return page.getResults().stream().map(movieTranslator::fromDTO).collect(toList());
    }

    //TODO: Change this to TMDBID which is what it actually is.
    public Optional<Movie> findByID(String tmdbId) {
        //TODO: catch client exception, if movie is not found
        MovieDto dto = restTemplate.getForObject(url + urlGetByID + tmdbId + "?api_key=" + apiKey, MovieDto.class);
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

    public List<Movie> findRecommendationsById(String id) {
        int pageNumber = 1;
        Set<MovieDto> movieDtoList = new LinkedHashSet<>();
        for (pageNumber = 1; pageNumber <= 2; pageNumber++) {
            var request = UriComponentsBuilder.fromHttpUrl(url + recommendations.replace("{movie_id}", id))
                    .queryParam("api_key", apiKey)
                    .queryParam("page", pageNumber).toUriString();
            var page = restTemplate.getForObject(request, ResultPageDto.class);
            if (page != null) {
                movieDtoList.addAll(page.getResults());
            }
        }
        return movieDtoList.stream().map(movieTranslator::fromDTO).collect(toList());
    }

    public List<Movie> findRecommendationsByTitle(String title) {
        var movie = findByTitle(title).stream().findFirst();
        String id;
        if (movie.isPresent()) {
            id = movie.get().getTmdbId();
        } else return new ArrayList<>();
        int pageNumber = 1;
        Set<MovieDto> movieDtoList = new LinkedHashSet<>();
        for (pageNumber = 1; pageNumber <= 2; pageNumber++) {
            var request = UriComponentsBuilder.fromHttpUrl(url + recommendations.replace("{movie_id}", id))
                    .queryParam("api_key", apiKey)
                    .queryParam("page", pageNumber).toUriString();
            var page = restTemplate.getForObject(request, ResultPageDto.class);
            if (page != null) {
                movieDtoList.addAll(page.getResults());
            }
        }
        return movieDtoList.stream().map(movieTranslator::fromDTO).collect(toList());
    }

}