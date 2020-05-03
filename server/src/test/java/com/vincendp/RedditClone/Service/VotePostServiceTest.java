package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.VotePostDTO;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Repository.VotePostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VotePostServiceTest {

    @InjectMocks
    private VotePostServiceImpl votePostService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VotePostRepository votePostRepository;

    private User user;

    private Post post;

    private Subreddit subreddit;

    private VotePostDTO votePostDTO;

    private VotePost votePost;

    @BeforeEach
    void init(){
        user = new User(UUID.randomUUID(), "bob", new Date());
        subreddit = new Subreddit(UUID.randomUUID(), "subreddit", new Date());
        post = new Post(UUID.randomUUID(), "title", new Date(), user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString()));
        votePostDTO = new VotePostDTO(user.getId().toString(), post.getId().toString(), true);
        votePost = new VotePost(new VotePostId(user, post), true);
    }

    @Test
    void when_invalid_user_should_throw_error(){
        votePostDTO.setUser_id("1");
        assertThrows(IllegalArgumentException.class, () -> {
            votePostService.createVotePost(votePostDTO);
        });
    }

    @Test
    void when_user_not_found_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            votePostService.createVotePost(votePostDTO);
        });
    }

    @Test
    void when_invalid_post_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(user);
        votePostDTO.setPost_id("1");
        assertThrows(IllegalArgumentException.class, () -> {
            votePostService.createVotePost(votePostDTO);
        });
    }

    @Test
    void when_post_not_found_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            votePostService.createVotePost(votePostDTO);
        });
    }

    @Test
    void when_create_vote_post_save_error_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenReturn(post);
        when(votePostRepository.save(any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> {
            votePostService.createVotePost(votePostDTO);
        });
    }

    @Test
    void when_create_vote_post_success_should_return_dto(){
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenReturn(post);
        when(votePostRepository.save(any())).thenReturn(votePost);

        VotePostDTO votePostDTO = votePostService.createVotePost(this.votePostDTO);

        assertNotNull(votePostDTO);
        assertEquals(votePostDTO.getPost_id(), this.votePostDTO.getPost_id());
        assertEquals(votePostDTO.getUser_id(), this.votePostDTO.getUser_id());
        assertEquals(votePostDTO.getVote(), this.votePostDTO.getVote());
    }

    @Test
    void when_vote_not_found_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenReturn(post);
        when(votePostRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            votePostService.updateVotePost(votePostDTO);
        });
    }

    @Test
    void when_update_vote_error_should_throw_error(){
        votePost.setVote(false);
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenReturn(post);
        when(votePostRepository.findById(any())).thenReturn(Optional.of(votePost));
        when(votePostRepository.save(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            votePostService.updateVotePost(votePostDTO);
        });
    }

    @Test
    void when_update_vote_success_should_have_no_errors(){
        votePost.setVote(false);
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenReturn(post);
        when(votePostRepository.findById(any())).thenReturn(Optional.of(votePost));
        when(votePostRepository.save(any())).thenReturn(votePost);

        assertDoesNotThrow(() -> {
            votePostService.updateVotePost(votePostDTO);
        });
    }

    @Test
    void when_delete_vote_error_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenReturn(post);
        when(votePostRepository.findById(any())).thenReturn(Optional.of(votePost));
        doThrow(IllegalArgumentException.class).when(votePostRepository).delete(any());

        assertThrows(RuntimeException.class, () -> {
            votePostService.deleteVotePost(votePostDTO);
        });
    }

    @Test
    void when_delete_vote_success_should_have_no_errors(){
        when(userRepository.getById(any())).thenReturn(user);
        when(postRepository.getById(any())).thenReturn(post);
        when(votePostRepository.findById(any())).thenReturn(Optional.of(votePost));

        assertDoesNotThrow(() -> {
            votePostService.deleteVotePost(votePostDTO);
        });
    }
}
