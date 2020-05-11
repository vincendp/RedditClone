package com.vincendp.RedditClone.Repository;

import com.vincendp.RedditClone.Dto.GetPostDTO;
import com.vincendp.RedditClone.Model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends CrudRepository<Post, UUID> {

    @Query(
    "SELECT NEW com.vincendp.RedditClone.Dto.GetPostDTO(" +
    "p.id, p.title, p.description, p.link, p.image_path, p.created_at, p.postType.id, p.user.id, " +
    "p.subreddit.id, u.username, s.name," +
    "SUM(CASE WHEN vp.vote IS NULL THEN 0 WHEN vp.vote = true THEN 1 WHEN vp.vote = false THEN -1 ELSE 0 END), " +
    "SUM(CASE WHEN vp.votePostId.user.id = :user_id AND vp.vote = true THEN 1 ELSE 0 END)) " +
    "FROM Post p " +
    "INNER JOIN User u ON (u.id = p.user.id) " +
    "INNER JOIN Subreddit s ON (s.id = p.subreddit.id) " +
    "LEFT JOIN VotePost vp ON (p.id = vp.votePostId.post.id) " +
    "WHERE p.id = :post_id " +
    "GROUP BY p.id, vp.vote"
    )
    GetPostDTO getPost(UUID post_id, UUID user_id);

    Post getById(UUID id);
}