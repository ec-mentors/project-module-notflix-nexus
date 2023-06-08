package io.everyonecodes.project.movie_recommendations.communication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResultPageDto {
    private int page;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_results")
    private int totalResults;
    private List<MovieDto> results;

    public int getPage() {
        return page;
    }

    public List<MovieDto> getResults() {
        return results;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setResults(List<MovieDto> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
