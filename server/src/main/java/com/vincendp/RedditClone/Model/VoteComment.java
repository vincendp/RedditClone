package com.vincendp.RedditClone.Model;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "VoteComment", indexes = { @Index(columnList = "user_id, comment_id") })
public class VoteComment {

    @EmbeddedId
    private VoteCommentId voteCommentId;

    @Column(columnDefinition = "TINYINT", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean vote;

    public VoteComment() {
    }

    public VoteComment(VoteCommentId voteCommentId, Boolean vote) {
        this.voteCommentId = voteCommentId;
        this.vote = vote;
    }

    public VoteCommentId getVoteCommentId() {
        return voteCommentId;
    }

    public void setVoteCommentId(VoteCommentId voteCommentId) {
        this.voteCommentId = voteCommentId;
    }

    public Boolean getVote() {
        return vote;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }
}
