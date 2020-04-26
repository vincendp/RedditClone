package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Exception.StorageException;
import com.vincendp.RedditClone.Model.Post;
import com.vincendp.RedditClone.Model.PostType;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.PostTypeRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubredditRepository subredditRepository;

    @Mock
    private PostTypeRepository postTypeRepository;

    @Mock
    private FileSystemStorageService storageService;

    private Subreddit subreddit;

    private User user;

    private CreatePostRequest createPostRequest;

    private Post post;

    private PostType postType;

    @BeforeEach
    void setup(){
        UUID subreddit_uuid = UUID.randomUUID();
        UUID user_uuid = UUID.randomUUID();
        UUID post_uuid = UUID.randomUUID();
        postType = new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString());
        createPostRequest = new CreatePostRequest("title", "description",
                "https://www.google.com",
                new MockMultipartFile("image", "image1.jpeg", "image/jpeg", "image1".getBytes()),
                subreddit_uuid.toString(), user_uuid.toString(), PostType.Type.TEXT.getValue());
        subreddit = new Subreddit(subreddit_uuid, "subreddit", new Date());
        user = new User(user_uuid, "bob", new Date());
        post = new Post(post_uuid, "title", new Date(), user, subreddit, postType);
    }

    @Test
    void when_subreddit_uuid_invalid_throws_error(){
        createPostRequest.setSubreddit_id("1");
        assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_user_uuid_invalid_throws_error(){
        createPostRequest.setUser_id("1");
        assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_subreddit_not_found_throws_error(){
        when(subredditRepository.getById(any())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_user_not_found_throws_error(){
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_post_type_error_throws_error(){
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_image_post_storage_service_error_throws_error(){
        createPostRequest.setPost_type(PostType.Type.IMAGE.getValue());
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);
        when(storageService.store(any())).thenThrow(StorageException.class);

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_post_save_error_throws_error() {
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);
        when(postTypeRepository.findById(anyInt())).thenReturn(Optional.of(postType));
        when(postRepository.save(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_text_post_success_returns_post_response(){
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);
        when(postTypeRepository.findById(anyInt())).thenReturn(Optional.of(postType));
        when(postRepository.save(any())).thenReturn(post);

        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertEquals(createPostRequest.getTitle(), createPostResponse.getTitle());
    }

    @Test
    void when_image_post_success_returns_post_response(){
        createPostRequest.setPost_type(PostType.Type.IMAGE.getValue());
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);
        when(postTypeRepository.findById(anyInt())).thenReturn(Optional.of(postType));
        when(storageService.store(any())).thenReturn(UUID.randomUUID().toString() + ".png");
        when(postRepository.save(any())).thenReturn(post);

        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertEquals(createPostRequest.getTitle(), createPostResponse.getTitle());
    }

    @Test
    void when_link_post_success_returns_post_response(){
        createPostRequest.setPost_type(PostType.Type.LINK.getValue());
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);
        when(postTypeRepository.findById(anyInt())).thenReturn(Optional.of(postType));
        when(postRepository.save(any())).thenReturn(post);

        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertEquals(createPostRequest.getTitle(), createPostResponse.getTitle());
    }

}
