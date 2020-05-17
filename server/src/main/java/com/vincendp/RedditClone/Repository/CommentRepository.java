package com.vincendp.RedditClone.Repository;

import com.vincendp.RedditClone.Dto.GetCommentDTO;
import com.vincendp.RedditClone.Model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends CrudRepository<Comment, UUID> {

    Comment getById(UUID id);

    @Query(
    "SELECT NEW com.vincendp.RedditClone.Dto.GetCommentDTO(" +
    "c.id, c.comment, c.created_at, u.id, u.username, " +
    "SUM(CASE WHEN vc.vote = true THEN 1 WHEN vc.vote = false THEN -1 ELSE 0 END), " +
    "SUM(CASE WHEN vc.voteCommentId.user.id = :user_id AND vc.vote = true THEN 1 " +
    "WHEN vc.voteCommentId.user.id = :user_id AND vc.vote = false THEN -1 ELSE 0 END)) " +
    "FROM Comment c " +
    "INNER JOIN User u ON (u.id = c.user.id) " +
    "LEFT JOIN VoteComment vc ON (vc.voteCommentId.comment.id = c.id) " +
    "WHERE c.post.id = :post_id " +
    "GROUP BY c.id")
    List<GetCommentDTO> getCommentsFromPost(UUID post_id, UUID user_id);

}
