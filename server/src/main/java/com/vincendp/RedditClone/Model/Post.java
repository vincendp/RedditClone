package com.vincendp.RedditClone.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Post", indexes = { @Index(columnList = "title, user_id, subreddit_id") })
public class Post {

    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name="uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(length = 65535, columnDefinition="Text")
    private String description;

    @Column(length = 1000)
    private String link;

    @Column
    private boolean deleted;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @ManyToOne
    private User user;

    @ManyToOne
    private Subreddit subreddit;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Subreddit getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
    }
}