package io.everyonecodes.project.movie_recommendations.logic;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.everyonecodes.project.movie_recommendations.communication.client.MovieApiClient;
import io.everyonecodes.project.movie_recommendations.communication.client.RequestController;
import io.everyonecodes.project.movie_recommendations.communication.dto.MovieDto;
import io.everyonecodes.project.movie_recommendations.communication.dto.MovieTranslator;
import io.everyonecodes.project.movie_recommendations.communication.dto.ResultPageDto;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Genre;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Keyword;
import io.everyonecodes.project.movie_recommendations.persistance.domain.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class RecommendationService {
    private final MovieApiClient client;
    private final MovieTranslator movieTranslator;
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String url;
    private final String urlDiscover;
    private final RequestController requestController;

    public RecommendationService(MovieApiClient client, MovieTranslator movieTranslator, RestTemplate restTemplate,
                                 @Value("${api.key}") String apiKey,
                                 @Value("${api.url}") String url,
                                 @Value("${api.discover}") String urlDiscover, RequestController requestController) {
        this.client = client;
        this.movieTranslator = movieTranslator;
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.url = url;
        this.urlDiscover = urlDiscover;
        this.requestController = requestController;
    }

    //TODO: reduce time complexity
    public List<Movie> recommendMovies(String movieIdOrTitle) {
        String input = movieIdOrTitle.trim();
        Movie inputMovie;
        Optional<Movie> oMovie;

        if (input.matches("\\d+")) {
            oMovie = client.findByID(input);
        } else {
            List<Movie> movieList = client.findByTitle(movieIdOrTitle);
            oMovie = movieList.stream().findFirst();
        }
        if (oMovie.isPresent()) {
            inputMovie = oMovie.get();
        } else {
            return List.of();
        }
        String baseUrl = url + urlDiscover;
        List<Long> listOfGenreId = inputMovie.getGenres().stream().map(Genre::getId).collect(toList());
        Set<Long> listOfKeywordId = client.getListOfKeywordsById(inputMovie.getTmdbID()).stream().map(Keyword::getId).collect(Collectors.toSet());
        List<Long> weightedGenres = keepAPercentageOfItems(100, listOfGenreId);
        String genreQueryParam = weightedGenres.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        Set<MovieDto> movieDtoSet = new HashSet<>();
        ResultPageDto page;
        for (int i = listOfKeywordId.size(); i >= 1; i--) {
            var combinations = Sets.combinations(listOfKeywordId, i);
            for (Set<Long> combination : combinations) {
                String keywordQueryParam = combination.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .queryParam("api_key", apiKey)
                        .queryParam("include_adult", false)
                        .queryParam("include_video", false)
                        .queryParam("page", 1)
                        .queryParam("sort_by", "popularity.desc")
                        .queryParam("with_genres", genreQueryParam)
                        .queryParam("with_keywords", keywordQueryParam);
                String request = builder.toUriString();
                page = Objects.requireNonNull(restTemplate.getForObject(request, ResultPageDto.class));
                movieDtoSet.addAll(page.getResults());
                if (movieDtoSet.size() >= 5) {
                    return movieDtoSet.stream().map(movieTranslator::fromDTO).collect(toList());
                }
            }
        }
        return movieDtoSet.stream().map(movieTranslator::fromDTO).collect(toList());
    }

    List<Long> keepAPercentageOfItems(int percentage, List<Long> list) {
        List<Long> listCopy = new ArrayList<>(list);
        int size = list.size();
        int itemsToKeep = (int) Math.ceil(size * percentage / 100.0);
        Collections.shuffle(listCopy);
        return listCopy.subList(0, itemsToKeep);
    }

//    private List<Long> pickRandomElements(int amount, List<Long> list) {
//        List<Long> listCopy = new ArrayList<>(list);
//        Collections.shuffle(listCopy);
//        return listCopy.subList(0, amount);
//        Collections2.permutations()
//    }
}
