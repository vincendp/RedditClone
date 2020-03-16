package com.vincendp.RedditClone.Dto;

import java.util.Date;

public class LoginResponse {

    String id;
    String username;
    Date created_at;

    public LoginResponse(){

    }

    public LoginResponse(String id, String username, Date created_at) {
        this.id = id;
        this.username = username;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
