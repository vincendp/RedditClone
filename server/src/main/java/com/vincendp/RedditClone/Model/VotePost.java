package com.vincendp.RedditClone.Model;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "VotePost", indexes = { @Index(columnList = "user_id, post_id") })
public class VotePost {

    @EmbeddedId
    private VotePostId votePostId;

    @Column(columnDefinition = "TINYINT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean vote;

    public VotePost() {
    }

    public VotePost(VotePostId votePostId, Boolean vote) {
        this.votePostId = votePostId;
        this.vote = vote;
    }

    public VotePostId getVotePostId() {
        return votePostId;
    }

    public void setVotePostId(VotePostId votePostId) {
        this.votePostId = votePostId;
    }

    public Boolean getVote() {
        return vote;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }
}
