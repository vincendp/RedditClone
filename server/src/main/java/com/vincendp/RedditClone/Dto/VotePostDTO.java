package com.vincendp.RedditClone.Dto;

public class VotePostDTO {

    private String user_id;
    private String post_id;
    private Boolean vote;

    public VotePostDTO() {
    }

    public VotePostDTO(String user_id, String post_id, Boolean vote) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.vote = vote;
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

    public Boolean getVote() {
        return vote;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }
}
