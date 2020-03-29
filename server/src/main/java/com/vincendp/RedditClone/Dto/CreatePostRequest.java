package com.vincendp.RedditClone.Dto;

public class CreatePostRequest {

    private String title;
    private String description;
    private String link;
    private String user_id;
    private String subreddit_id;

    public CreatePostRequest() {

    }

    public CreatePostRequest(String title, String description, String user_id, String subreddit_id) {
        this.title = title;
        this.description = description;
        this.user_id = user_id;
        this.subreddit_id = subreddit_id;
    }

    public CreatePostRequest(String title, String description, String link, String user_id, String subreddit_id) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.user_id = user_id;
        this.subreddit_id = subreddit_id;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSubreddit_id() {
        return subreddit_id;
    }

    public void setSubreddit_id(String subreddit_id) {
        this.subreddit_id = subreddit_id;
    }
}

