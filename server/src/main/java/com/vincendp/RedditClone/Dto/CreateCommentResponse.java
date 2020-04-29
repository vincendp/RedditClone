package com.vincendp.RedditClone.Dto;

import java.util.Date;

public class CreateCommentResponse {
    private String id;
    private String comment;
    private String user_id;
    private String post_id;
    private Date created_at;

    public CreateCommentResponse() {
    }

    public CreateCommentResponse(String id, String comment, String user_id, String post_id, Date created_at) {
        this.id = id;
        this.comment = comment;
        this.user_id = user_id;
        this.post_id = post_id;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
