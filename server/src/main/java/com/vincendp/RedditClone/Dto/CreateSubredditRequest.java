package com.vincendp.RedditClone.Dto;

public class CreateSubredditRequest {

    private String name;

    public CreateSubredditRequest(){

    }

    public CreateSubredditRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
