package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Dto.GetPostDTO;
import com.vincendp.RedditClone.Dto.GetPostPreviewDTO;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Exception.StorageException;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.PostTypeRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Utility.ProjectionUtility;
import com.vincendp.RedditClone.Utility.SecurityContextUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

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

    @Mock
    private SecurityContextUtility securityContextUtility;

    @Mock
    private ProjectionUtility projectionUtility;

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
    void when_create_post_and_subreddit_uuid_invalid_throws_error(){
        createPostRequest.setSubreddit_id("1");
        assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_create_post_and_user_uuid_invalid_throws_error(){
        createPostRequest.setUser_id("1");
        assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_create_post_and_subreddit_not_found_throws_error(){
        when(subredditRepository.getById(any())).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_create_post_and_user_not_found_throws_error(){
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_create_post_and_post_type_error_throws_error(){
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_create_post_and_image_post_storage_service_error_throws_error(){
        createPostRequest.setPost_type(PostType.Type.IMAGE.getValue());
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);
        when(storageService.store(any())).thenThrow(StorageException.class);

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_create_post_and_post_save_error_throws_error() {
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);
        when(postTypeRepository.findById(anyInt())).thenReturn(Optional.of(postType));
        when(postRepository.save(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_create_post_and_text_post_success_returns_post_response(){
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);
        when(postTypeRepository.findById(anyInt())).thenReturn(Optional.of(postType));
        when(postRepository.save(any())).thenReturn(post);

        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertEquals(createPostRequest.getTitle(), createPostResponse.getTitle());
    }

    @Test
    void when_create_post_and_image_post_success_returns_post_response(){
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
    void when_create_post_and_link_post_success_returns_post_response(){
        createPostRequest.setPost_type(PostType.Type.LINK.getValue());
        when(subredditRepository.getById(any())).thenReturn(subreddit);
        when(userRepository.getById(any())).thenReturn(user);
        when(postTypeRepository.findById(anyInt())).thenReturn(Optional.of(postType));
        when(postRepository.save(any())).thenReturn(post);

        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertEquals(createPostRequest.getTitle(), createPostResponse.getTitle());
    }

    @Test
    void when_get_post_uuid_invalid_should_throw_error(){
        assertThrows(IllegalArgumentException.class, () -> {
            postService.getPost("1");
        });
    }

    @Test
    void when_get_post_with_auth_and_post_not_found_should_throw_error(){
        when(securityContextUtility.getUserDetailsFromSecurityContext()).thenReturn(
                new CustomUserDetails(user, new UserAuthentication())
        );
        assertThrows(ResourceNotFoundException.class, () -> {
            postService.getPost(UUID.randomUUID().toString());
        });
    }

    @Test
    void when_get_post_with_no_auth_and_post_not_found_should_throw_error(){
        assertThrows(ResourceNotFoundException.class, () -> {
            postService.getPost(UUID.randomUUID().toString());
        });
    }

    @Test
    void when_get_post_success_then_return_dto(){
        when(postRepository.getPost(any(UUID.class), any())).thenReturn(new GetPostDTO());
        GetPostDTO getPostDTO = postService.getPost(UUID.randomUUID().toString());
        assertNotNull(getPostDTO);
    }

    @Test
    void when_get_all_post_previews_and_repository_error_should_throw_error(){
        when(postRepository.getAllPostPreviews(any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> {
            postService.getAllPostPreviews();
        });
    }

    @Test
    void when_get_all_post_previews_and_success_with_auth_should_return_list(){
        when(securityContextUtility.getUserDetailsFromSecurityContext()).thenReturn(
                new CustomUserDetails(user, new UserAuthentication())
        );
        when(projectionUtility.getPostPreviewDTO(anyList())).thenReturn(new ArrayList<>(Arrays.asList(new GetPostPreviewDTO())));

        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviews();
        assertNotNull(postPreviews);
    }

    @Test
    void when_get_all_post_previews_and_success_without_auth_should_return_list(){
        when(projectionUtility.getPostPreviewDTO(anyList())).thenReturn(new ArrayList<>(Arrays.asList(new GetPostPreviewDTO())));

        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviews();
        assertNotNull(postPreviews);
    }

    @Test
    void when_get_all_user_post_previews_and_invalid_uuid_should_throw_error(){
        assertThrows(IllegalArgumentException.class, () -> {
           postService.getAllPostPreviewsByUser("1");
        });
    }

    @Test
    void when_get_all_user_post_previews_and_repository_error_should_throw_error(){
        when(postRepository.getAllPostPreviewsByUser(any(), any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> {
            postService.getAllPostPreviewsByUser(UUID.randomUUID().toString());
        });
    }

    @Test
    void when_get_all_user_post_previews_and_success_with_auth_should_return_list(){
        when(securityContextUtility.getUserDetailsFromSecurityContext()).thenReturn(
                new CustomUserDetails(user, new UserAuthentication())
        );
        when(projectionUtility.getPostPreviewDTO(anyList())).thenReturn(new ArrayList<>(Arrays.asList(new GetPostPreviewDTO())));

        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviewsByUser(UUID.randomUUID().toString());
        assertNotNull(postPreviews);
    }

    @Test
    void when_get_all_user_post_previews_and_success_without_auth_should_return_list(){
        when(securityContextUtility.getUserDetailsFromSecurityContext()).thenReturn(
                new CustomUserDetails(user, new UserAuthentication())
        );
        when(projectionUtility.getPostPreviewDTO(anyList())).thenReturn(new ArrayList<>(Arrays.asList(new GetPostPreviewDTO())));

        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviewsByUser(UUID.randomUUID().toString());
        assertNotNull(postPreviews);
    }

    @Test
    void when_get_all_subreddit_post_previews_and_invalid_uuid_should_throw_error(){
        assertThrows(IllegalArgumentException.class, () -> {
            postService.getAllPostPreviewsBySubreddit("1");
        });
    }

    @Test
    void when_get_all_subreddit_post_previews_and_repository_error_should_throw_error(){
        when(postRepository.getAllPostPreviewsBySubreddit(any(), any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> {
            postService.getAllPostPreviewsBySubreddit(UUID.randomUUID().toString());
        });
    }

    @Test
    void when_get_all_subreddit_post_previews_and_success_with_auth_should_return_list(){
        when(securityContextUtility.getUserDetailsFromSecurityContext()).thenReturn(
                new CustomUserDetails(user, new UserAuthentication())
        );
        when(projectionUtility.getPostPreviewDTO(anyList())).thenReturn(new ArrayList<>(Arrays.asList(new GetPostPreviewDTO())));

        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviewsBySubreddit(UUID.randomUUID().toString());
        assertNotNull(postPreviews);
    }

    @Test
    void when_get_all_subreddit_post_previews_and_success_without_auth_should_return_list(){
        when(securityContextUtility.getUserDetailsFromSecurityContext()).thenReturn(
                new CustomUserDetails(user, new UserAuthentication())
        );
        when(projectionUtility.getPostPreviewDTO(anyList())).thenReturn(new ArrayList<>(Arrays.asList(new GetPostPreviewDTO())));

        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviewsBySubreddit(UUID.randomUUID().toString());
        assertNotNull(postPreviews);
    }

}
