package com.vincendp.RedditClone.Repository;

import com.vincendp.RedditClone.Dto.GetPostDTO;
import com.vincendp.RedditClone.Dto.GetPostPreviewDTO;
import com.vincendp.RedditClone.Model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends CrudRepository<Post, UUID> {

    Post getById(UUID id);

    @Query(
    "SELECT NEW com.vincendp.RedditClone.Dto.GetPostDTO(" +
    "p.id, p.title, p.description, p.link, p.image_path, p.created_at, p.postType.id, p.user.id, " +
    "p.subreddit.id, u.username, s.name, " +
    "SUM(CASE WHEN vp.vote = true THEN 1 WHEN vp.vote = false THEN -1 ELSE 0 END), " +
    "SUM(CASE WHEN vp.votePostId.user.id = :auth_user_id AND vp.vote = true THEN 1 " +
    "WHEN vp.votePostId.user.id = :auth_user_id AND vp.vote = false THEN -1 ELSE 0 END )) " +
    "FROM Post p " +
    "INNER JOIN User u ON (u.id = p.user.id) " +
    "INNER JOIN Subreddit s ON (s.id = p.subreddit.id) " +
    "LEFT JOIN VotePost vp ON (p.id = vp.votePostId.post.id) " +
    "WHERE p.id = :post_id " +
    "GROUP BY p.id"
    )
    GetPostDTO getPost(UUID post_id, UUID auth_user_id);


    @Query(value =
    "SELECT p.id, p.title, p.link, p.image_path, p.created_at, p.post_type_id, p.user_id," +
    "p.subreddit_id, u.username, s.name, " +
    "SUM(CASE WHEN vp.vote = true THEN 1 WHEN vp.vote = false THEN -1 ELSE 0 END), " +
    "SUM(CASE WHEN vp.user_id = :auth_user_id AND vp.vote = true THEN 1 " +
    "WHEN vp.user_id = :auth_user_id AND vp.vote = false THEN -1 ELSE 0 END ), " +
    "(SELECT COUNT(*) FROM Comment c WHERE c.post_id = p.id GROUP BY p.id) " +
    "FROM Post p " +
    "INNER JOIN User u ON (u.id = p.user_id) " +
    "INNER JOIN Subreddit s ON (s.id = p.subreddit_id) " +
    "LEFT JOIN VotePost vp ON (vp.post_id = p.id) " +
    "GROUP BY p.id"
    , nativeQuery= true
    )
    List<Object[]> getAllPostPreviews(UUID auth_user_id);


    @Query(value =
    "SELECT p.id, p.title, p.link, p.image_path, p.created_at, p.post_type_id, p.user_id," +
    "p.subreddit_id, u.username, s.name, " +
    "SUM(CASE WHEN vp.vote = true THEN 1 WHEN vp.vote = false THEN -1 ELSE 0 END), " +
    "SUM(CASE WHEN vp.user_id = :auth_user_id AND vp.vote = true THEN 1 " +
    "WHEN vp.user_id = :auth_user_id AND vp.vote = false THEN -1 ELSE 0 END ), " +
    "(SELECT COUNT(*) FROM Comment c WHERE c.post_id = p.id GROUP BY p.id) " +
    "FROM Post p " +
    "INNER JOIN User u ON (u.id = p.user_id) " +
    "INNER JOIN Subreddit s ON (s.id = p.subreddit_id) " +
    "LEFT JOIN VotePost vp ON (vp.post_id = p.id) " +
    "WHERE p.user_id = :posted_by_user_id " +
    "GROUP BY p.id"
    , nativeQuery= true
    )
    List<Object[]> getAllPostPreviewsByUser(UUID posted_by_user_id, UUID auth_user_id);


    @Query(value =
    "SELECT p.id, p.title, p.link, p.image_path, p.created_at, p.post_type_id, p.user_id," +
    "p.subreddit_id, u.username, s.name, " +
    "SUM(CASE WHEN vp.vote = true THEN 1 WHEN vp.vote = false THEN -1 ELSE 0 END), " +
    "SUM(CASE WHEN vp.user_id = :auth_user_id AND vp.vote = true THEN 1 " +
    "WHEN vp.user_id = :auth_user_id AND vp.vote = false THEN -1 ELSE 0 END ), " +
    "(SELECT COUNT(*) FROM Comment c WHERE c.post_id = p.id GROUP BY p.id) " +
    "FROM Post p " +
    "INNER JOIN User u ON (u.id = p.user_id) " +
    "INNER JOIN Subreddit s ON (s.id = p.subreddit_id) " +
    "LEFT JOIN VotePost vp ON (vp.post_id = p.id) " +
    "WHERE p.subreddit_id = :subreddit_id " +
    "GROUP BY p.id"
    , nativeQuery= true
    )
    List<Object[]> getAllPostPreviewsBySubreddit(UUID subreddit_id, UUID auth_user_id);

}