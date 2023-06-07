package io.everyonecodes.project.movie_recommendations.persistance.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Genre {
    @Id
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    public Genre() {
    }

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {return id;}
    public String getName() {return name;}

    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
}
