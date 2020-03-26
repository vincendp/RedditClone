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


}