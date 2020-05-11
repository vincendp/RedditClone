package com.vincendp.RedditClone.Dto;

import java.util.Date;
import java.util.UUID;

public class GetPostDTO{

    String post_id;
    String title;
    String description;
    String link;
    String image_path;
    Date created_at;
    Integer post_type_id;
    String user_id;
    String subreddit_id;
    String username;
    String subreddit;
    Integer votes;
    Boolean user_voted_for_post;

    public GetPostDTO() {
    }

    public GetPostDTO(UUID post_id, String title, String description, String link, String image_path, Date created_at,
                      Integer post_type_id, UUID user_id, UUID subreddit_id, String username, String subreddit,
                      Long votes, Long user_voted_for_post) {
        this.post_id = post_id.toString();
        this.title = title;
        this.description = description;
        this.link = link;
        this.image_path = image_path;
        this.created_at = created_at;
        this.post_type_id = post_type_id;
        this.user_id = user_id.toString();
        this.subreddit_id = subreddit_id.toString();
        this.username = username;
        this.subreddit = subreddit;
        this.votes = votes.intValue();
        this.user_voted_for_post = user_voted_for_post >= 1;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Integer getPost_type_id() {
        return post_type_id;
    }

    public void setPost_type_id(Integer post_type_id) {
        this.post_type_id = post_type_id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Boolean getUser_voted_for_post() {
        return user_voted_for_post;
    }

    public void setUser_voted_for_post(Boolean user_voted_for_post) {
        this.user_voted_for_post = user_voted_for_post;
    }
}
