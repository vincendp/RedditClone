package com.vincendp.RedditClone.Dto;

public class VoteCommentDTO {
    private String user_id;
    private String comment_id;
    private Boolean vote;

    public VoteCommentDTO() {
    }

    public VoteCommentDTO(String user_id, String comment_id, Boolean vote) {
        this.user_id = user_id;
        this.comment_id = comment_id;
        this.vote = vote;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public Boolean getVote() {
        return vote;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }
}
