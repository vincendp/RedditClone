package com.vincendp.RedditClone.Repository;

import com.vincendp.RedditClone.Model.VotePost;
import com.vincendp.RedditClone.Model.VotePostId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotePostRepository extends CrudRepository<VotePost, VotePostId> {
}
