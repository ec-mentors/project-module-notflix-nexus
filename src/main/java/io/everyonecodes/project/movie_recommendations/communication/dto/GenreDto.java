package io.everyonecodes.project.movie_recommendations.communication.dto;

public class GenreDto {
    private int id;
    private String name;

    public GenreDto() {}

    public GenreDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {return id;}
    public String getName() {return name;}

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
}
