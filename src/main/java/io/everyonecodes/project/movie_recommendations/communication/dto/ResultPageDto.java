package io.everyonecodes.project.movie_recommendations.communication.dto;

import java.util.List;

public class ResultPageDto {
    private int page;
    private List<MovieDto> results;

    public int getPage() {return page;}
    public List<MovieDto> getResults() {return results;}

    public void setPage(int page) {this.page = page;}
    public void setResults(List<MovieDto> results) {this.results = results;}
}
