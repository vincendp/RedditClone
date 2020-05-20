package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateCommentRequest;
import com.vincendp.RedditClone.Dto.CreateCommentResponse;
import com.vincendp.RedditClone.Dto.GetCommentDTO;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.CommentRepository;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Utility.SecurityContextUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContextUtility securityContextUtility;

    private Subreddit subreddit;

    private Post post;

    private User user;

    private CreateCommentRequest createCommentRequest;

    @BeforeEach
    void init(){
        user = new User(UUID.randomUUID(), "bob", new Date());
        subreddit = new Subreddit(UUID.randomUUID(), "subreddit", new Date());
        post = new Post(UUID.randomUUID(), "title", new Date(), user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString()));
        createCommentRequest = new CreateCommentRequest("Comment", user.getId().toString(), post.getId().toString());
    }

    @Test
    void when_create_comment_and_user_uuid_invalid_throw_error(){
        createCommentRequest.setUser_id("1");
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.createComment(createCommentRequest);
        });
    }

    @Test
    void when_create_comment_and_post_uuid_invalid_throw_error(){
        createCommentRequest.setPost_id("1");
        assertThrows(IllegalArgumentException.class, () -> {
            commentService.createComment(createCommentRequest);
        });
    }

    @Test
    void when_create_comment_and_user_not_found_throws_error(){
        when(userRepository.getById(any())).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(createCommentRequest);
        });
    }

    @Test
    void when_create_comment_and_post_not_found_throws_error(){
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(createCommentRequest);
        });
    }

    @Test
    void when_create_comment_and_comment_save_error_throws_error(){
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenReturn(post);
        when(commentRepository.save(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            commentService.createComment(createCommentRequest);
        });
    }

    @Test
    void when_create_comment_and_comment_save_success_return_comment_response(){
        Comment comment = new Comment(UUID.randomUUID(),
                createCommentRequest.getComment() ,false, new Date(), user, post);
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenReturn(post);
        when(commentRepository.save(any())).thenReturn(comment);

        CreateCommentResponse createCommentResponse = commentService.createComment(createCommentRequest);

        assertNotNull(createCommentResponse);
        assertEquals(createCommentResponse.getComment(), createCommentRequest.getComment());
    }

    @Test
    void when_get_comment_invalid_post_id_then_throw_error(){
        assertThrows(IllegalArgumentException.class, () -> {
           commentService.getCommentsFromPost("1");
        });
    }

    @Test
    void when_get_comment_post_not_found_throws_error(){
        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.getCommentsFromPost(UUID.randomUUID().toString());
        });
    }

    @Test
    void when_get_comment_with_auth_success_then_returns_dtos(){
        when(postRepository.getById(any(UUID.class))).thenReturn(post);
        when(securityContextUtility.getUserDetailsFromSecurityContext()).thenReturn(
                new CustomUserDetails(user, new UserAuthentication()));
        when(commentRepository.getCommentsFromPost(any(UUID.class), any())).thenReturn(
                new ArrayList<>(Arrays.asList(new GetCommentDTO())));
        List<GetCommentDTO> dtos = commentService.getCommentsFromPost(UUID.randomUUID().toString());
        assertNotNull(dtos);
    }

    @Test
    void when_get_comment_with_no_auth_success_then_returns_dtos(){
        when(postRepository.getById(any(UUID.class))).thenReturn(post);
        when(securityContextUtility.getUserDetailsFromSecurityContext()).thenReturn(null);
        when(commentRepository.getCommentsFromPost(any(UUID.class), any())).thenReturn(
                new ArrayList<>(Arrays.asList(new GetCommentDTO())));
        List<GetCommentDTO> dtos = commentService.getCommentsFromPost(UUID.randomUUID().toString());
        assertNotNull(dtos);
    }
}
