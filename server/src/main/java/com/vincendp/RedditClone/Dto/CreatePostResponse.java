package com.vincendp.RedditClone.Dto;

import java.util.Date;

public class CreatePostResponse {

    private String id;
    private String title;
    private String description;
    private String link;
    private String user_id;
    private String subreddit_id;
    private Date created_at;

    public CreatePostResponse(){

    }

    public CreatePostResponse(String id, String title, String description, String link, String user_id, String subreddit_id, Date created_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.user_id = user_id;
        this.subreddit_id = subreddit_id;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
