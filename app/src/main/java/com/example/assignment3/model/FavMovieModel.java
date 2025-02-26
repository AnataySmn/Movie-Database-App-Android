package com.example.assignment3.model;

public class FavMovieModel {
    public String title;
    public String poster;
    public String description;

    public FavMovieModel(String title, String poster, String description) {
        this.title = title;
        this.poster = poster;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
