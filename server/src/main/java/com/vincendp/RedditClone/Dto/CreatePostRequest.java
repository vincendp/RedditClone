package com.vincendp.RedditClone.Dto;

public class CreatePostRequest {

    private String title;
    private String description;
    private String link;

    public CreatePostRequest() {

    }

    public CreatePostRequest(String title) {
        this.title = title;
    }

    public CreatePostRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public CreatePostRequest(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

