package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateCommentRequest;
import com.vincendp.RedditClone.Dto.CreateCommentResponse;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.Post;
import com.vincendp.RedditClone.Model.PostType;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.CommentRepository;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql", "/sql/data.sql"})
public class CommentServiceIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    private Subreddit subreddit;

    private Post post;

    private User user;

    private CreateCommentRequest createCommentRequest;

    @BeforeEach
    void init(){
        user = new User(null, "bob", null);
        subreddit = new Subreddit(null, "subreddit", null);

        userRepository.save(user);
        subredditRepository.save(subreddit);

        post = new Post(null, "title", null, user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString()));

        postRepository.save(post);

        createCommentRequest = new CreateCommentRequest("Comment"
                , user.getId().toString(), post.getId().toString());
    }

    @Test
    void when_user_not_found_throw_error(){
        createCommentRequest.setUser_id(UUID.randomUUID().toString());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(createCommentRequest);
        });
    }

    @Test
    void when_post_not_found_throw_error(){
        createCommentRequest.setPost_id(UUID.randomUUID().toString());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(createCommentRequest);
        });
    }

    @Test
    void when_valid_comment_should_return_response(){
        CreateCommentResponse createCommentResponse = commentService.createComment(createCommentRequest);

        assertNotNull(createCommentResponse);
        assertNotNull(createCommentResponse.getId());
        assertEquals(createCommentResponse.getComment(), createCommentRequest.getComment());
    }
}
