package com.vincendp.RedditClone.Dto;

import java.util.Date;
import java.util.UUID;

public class GetCommentDTO {

    String comment_id;
    String comment;
    Date created_at;
    String user_id;
    String username;
    Long votes;
    Long user_voted_for_comment;

    public GetCommentDTO() {
    }

    public GetCommentDTO(UUID comment_id, String comment, Date created_at, UUID user_id, String username, Long votes, Long user_voted_for_comment) {
        this.comment_id = comment_id.toString();
        this.comment = comment;
        this.created_at = created_at;
        this.user_id = user_id.toString();
        this.username = username;
        this.votes = votes;
        this.user_voted_for_comment = user_voted_for_comment;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }

    public Long getUser_voted_for_comment() {
        return user_voted_for_comment;
    }

    public void setUser_voted_for_comment(Long user_voted_for_comment) {
        this.user_voted_for_comment = user_voted_for_comment;
    }
}
