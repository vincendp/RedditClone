package com.vincendp.RedditClone.Dto;

import java.util.Date;

public class CreateUserResponse {

    private String username;
    private Date created_at;

    public CreateUserResponse(){
    }

    public CreateUserResponse(String username, Date created_at) {
        this.username = username;
        this.created_at = created_at;
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
