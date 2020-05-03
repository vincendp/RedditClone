package com.vincendp.RedditClone.Repository;

import com.vincendp.RedditClone.Model.VoteComment;
import com.vincendp.RedditClone.Model.VoteCommentId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteCommentRepository extends CrudRepository<VoteComment, VoteCommentId> {
}
