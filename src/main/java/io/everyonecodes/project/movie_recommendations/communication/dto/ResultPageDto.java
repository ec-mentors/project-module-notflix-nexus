package io.everyonecodes.project.movie_recommendations.communication.dto;

import java.util.List;

public class ResultPageDto {
    private int page;
    private int total_pages;
    private int total_results;
    private List<MovieDto> results;

    public int getPage() {return page;}
    public List<MovieDto> getResults() {return results;}

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public void setPage(int page) {this.page = page;}
    public void setResults(List<MovieDto> results) {this.results = results;}
}
