package com.vincendp.RedditClone.Dto;

import com.vincendp.RedditClone.Model.PostType;
import org.springframework.web.multipart.MultipartFile;

public class CreatePostRequest {

    private String title;
    private String description;
    private String link;
    private MultipartFile image;
    private String user_id;
    private String subreddit_id;
    private Integer post_type;

    public CreatePostRequest() {

    }

    public CreatePostRequest(String title, String description, String link, MultipartFile image, String user_id, String subreddit_id, Integer post_type) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.image = image;
        this.user_id = user_id;
        this.subreddit_id = subreddit_id;
        this.post_type = post_type;
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

    public MultipartFile getImage() { return image; }

    public void setImage(MultipartFile image) { this.image = image; }

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

    public Integer getPost_type() {
        return post_type;
    }

    public void setPost_type(Integer post_type) {
        this.post_type = post_type;
    }
}

