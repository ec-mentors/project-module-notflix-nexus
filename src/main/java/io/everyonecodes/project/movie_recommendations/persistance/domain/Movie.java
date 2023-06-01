package io.everyonecodes.project.movie_recommendations.persistance.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String title;
    private String genre;
    private int releaseYear;

    public Long getId() {return id;}
    public String getTitle() {return title;}
    public String getGenre() {return genre;}
    public int getReleaseYear() {return releaseYear;}

    public void setId(Long id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setGenre(String genre) {this.genre = genre;}
    public void setReleaseYear(int releaseYear) {this.releaseYear = releaseYear;}
}