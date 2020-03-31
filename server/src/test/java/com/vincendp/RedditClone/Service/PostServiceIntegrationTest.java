package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.Post;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql"})
public class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    private CreatePostRequest createPostRequest;

    private Subreddit subreddit;

    private User user;

    private Post post;

    @BeforeEach
    void setup(){
        subreddit = new Subreddit(null, "subreddit", new Date());
        user = new User(null, "bob", new Date());
        post = new Post(null, "title", "description", null, false, new Date(), user, subreddit);

        subredditRepository.save(subreddit);
        userRepository.save(user);
        createPostRequest = new CreatePostRequest("title", "description",user.getId().toString(), subreddit.getId().toString());
    }

    @Test
    void when_subreddit_not_found_should_throw_error(){
        createPostRequest.setSubreddit_id(UUID.randomUUID().toString());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_user_not_found_should_throw_error(){
        createPostRequest.setUser_id(UUID.randomUUID().toString());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_valid_user_and_subreddit_should_return_response(){
        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);

        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddit.getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(user.getId().toString(), createPostResponse.getUser_id());
        assertEquals(post.getTitle(), createPostResponse.getTitle());
    }
}
