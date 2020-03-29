package com.vincendp.RedditClone.Dto;

import java.util.Date;

public class CreateSubredditResponse {

    private String id;
    private String name;
    private Date created_at;

    public CreateSubredditResponse(){

    }

    public CreateSubredditResponse(String id, String name, Date created_at) {
        this.id = id;
        this.name = name;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
