package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Dto.GetPostDTO;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql", "/sql/data.sql"})
public class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private PostTypeRepository postTypeRepository;

    @Autowired
    private VotePostRepository votePostRepository;

    private CreatePostRequest createPostRequest;

    private Subreddit subreddit;

    private User[] users;

    private Post post;

    private UserAuthentication userAuthentication;

    @BeforeEach
    void setup(){
        users = new User[6];
        subreddit = new Subreddit(null, "subreddit", new Date());
        subredditRepository.save(subreddit);
        users[0] = userRepository.save(new User(null, "bob", new Date()));
        users[1] = userRepository.save(new User(null, "bob2", new Date()));
        users[2] = userRepository.save(new User(null, "bob3", new Date()));
        users[3] = userRepository.save(new User(null, "bob4", new Date()));
        users[4] = userRepository.save(new User(null, "bob5", new Date()));
        users[5] = userRepository.save(new User(null, "bob6", new Date()));

        userAuthentication = userAuthenticationRepository.save(new UserAuthentication(users[0].getId(), "123456", users[0]));

        PostType postType = postTypeRepository.findById(PostType.Type.TEXT.getValue()).get();
        createPostRequest = new CreatePostRequest("title", "description",
                "https://www.google.com",
                new MockMultipartFile("image", "image1.jpeg", "image/jpeg", "image1".getBytes()),
                users[0].getId().toString(), subreddit.getId().toString(), PostType.Type.TEXT.getValue());

        post = new Post(null, "title", new Date(), users[0], subreddit, postType);

        postRepository.save(post);
        votePostRepository.save(new VotePost(new VotePostId(users[0], post), true));
        votePostRepository.save(new VotePost(new VotePostId(users[1], post), true));
        votePostRepository.save(new VotePost(new VotePostId(users[2], post), false));
        votePostRepository.save(new VotePost(new VotePostId(users[3], post), true));
        votePostRepository.save(new VotePost(new VotePostId(users[4], post), true));
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
    void when_post_type_not_found_should_throw_error(){
        createPostRequest.setPost_type(-1);

        assertThrows(NoSuchElementException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_image_post_with_invalid_image_should_throw_error(){
        createPostRequest.setPost_type(PostType.Type.IMAGE.getValue());
        createPostRequest.setImage(null);

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });

        createPostRequest.setImage(
                new MockMultipartFile("image", "image1.jpeg",
                        "image/jpeg", new byte[0]));

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_link_post_with_no_link_should_throw_error(){
        createPostRequest.setPost_type(PostType.Type.LINK.getValue());
        createPostRequest.setLink(null);

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_link_post_with_invalid_link_should_throw_error(){
        createPostRequest.setPost_type(PostType.Type.LINK.getValue());
        createPostRequest.setLink("www.");

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_valid_text_post_should_return_response(){
        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);

        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddit.getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(users[0].getId().toString(), createPostResponse.getUser_id());
        assertEquals(post.getTitle(), createPostResponse.getTitle());
    }

    @Test
    void when_valid_image_post_should_return_response(){
        createPostRequest.setPost_type(PostType.Type.IMAGE.getValue());

        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddit.getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(users[0].getId().toString(), createPostResponse.getUser_id());
        assertEquals(post.getTitle(), createPostResponse.getTitle());

        createPostRequest.setImage(
                new MockMultipartFile("image", "image1.jpg",
                        "image/jpg", "image".getBytes()));

        createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddit.getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(users[0].getId().toString(), createPostResponse.getUser_id());
        assertEquals(post.getTitle(), createPostResponse.getTitle());

        createPostRequest.setImage(
                new MockMultipartFile("image", "image1.png",
                        "image/png", "image".getBytes()));

        createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddit.getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(users[0].getId().toString(), createPostResponse.getUser_id());
        assertEquals(post.getTitle(), createPostResponse.getTitle());
    }

    @Test
    void when_valid_link_post_should_return_response(){
        createPostRequest.setPost_type(PostType.Type.LINK.getValue());
        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);

        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddit.getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(users[0].getId().toString(), createPostResponse.getUser_id());
        assertEquals(post.getTitle(), createPostResponse.getTitle());
    }

    @Test
    void when_post_not_found_throws_error(){
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[0], userAuthentication)
                        , null));
        assertThrows(ResourceNotFoundException.class, () -> {
            postService.getPost(UUID.randomUUID().toString());
        });
    }

    @Test
    void when_auth_user_with_post_created_by_user_returns_dto(){
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[0], userAuthentication)
                , null));
        GetPostDTO getPostDTO = postService.getPost(post.getId().toString());
        assertNotNull(getPostDTO);
        assertEquals(getPostDTO.getTitle(), post.getTitle());
        assertTrue(getPostDTO.getUser_voted_for_post());
        assertEquals(3, getPostDTO.getVotes());
    }

    @Test
    void when_auth_user_with_post_not_created_by_user_returns_dto(){
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[5], userAuthentication)
                        , null));
        GetPostDTO getPostDTO = postService.getPost(post.getId().toString());
        assertNotNull(getPostDTO);
        assertEquals(getPostDTO.getTitle(), post.getTitle());
        assertFalse(getPostDTO.getUser_voted_for_post());
        assertEquals(3, getPostDTO.getVotes());
    }

    @Test
    void when_no_auth_user_returns_dto(){
        GetPostDTO getPostDTO = postService.getPost(post.getId().toString());
        assertNotNull(getPostDTO);
        assertEquals(getPostDTO.getTitle(), post.getTitle());
        assertFalse(getPostDTO.getUser_voted_for_post());
        assertEquals(3, getPostDTO.getVotes());
    }
}
