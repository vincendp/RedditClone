package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.VoteCommentDTO;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.*;
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
public class VoteCommentServiceTest {

    @InjectMocks
    private VoteCommentServiceImpl voteCommentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private VoteCommentRepository voteCommentRepository;

    private User user;

    private Post post;

    private Subreddit subreddit;

    private Comment comment;

    private VoteComment voteComment;

    private VoteCommentDTO voteCommentDTO;

    @BeforeEach
    void init(){
        user = new User(UUID.randomUUID(), "bob", new Date());
        subreddit = new Subreddit(UUID.randomUUID(), "subreddit", new Date());
        post = new Post(UUID.randomUUID(), "title", new Date(), user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString()));
        comment = new Comment(UUID.randomUUID(), "Comment", false, new Date(), user, post);

        voteCommentDTO = new VoteCommentDTO(user.getId().toString(), comment.getId().toString(), true);
        voteComment = new VoteComment(new VoteCommentId(user, comment), true);
    }

    @Test
    void when_invalid_user_should_throw_error(){
        voteCommentDTO.setUser_id("1");
        assertThrows(IllegalArgumentException.class, () -> {
            voteCommentService.createVoteComment(voteCommentDTO);
        });
    }

    @Test
    void when_user_not_found_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            voteCommentService.createVoteComment(voteCommentDTO);
        });
    }

    @Test
    void when_invalid_comment_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(user);
        voteCommentDTO.setComment_id("1");
        assertThrows(IllegalArgumentException.class, () -> {
            voteCommentService.createVoteComment(voteCommentDTO);
        });
    }

    @Test
    void when_comment_not_found_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(user);
        when(commentRepository.getById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            voteCommentService.createVoteComment(voteCommentDTO);
        });
    }

    @Test
    void when_create_vote_comment_save_error_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(user);
        when(commentRepository.getById(any())).thenReturn(comment);
        when(voteCommentRepository.save(any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> {
            voteCommentService.createVoteComment(voteCommentDTO);
        });
    }

    @Test
    void when_create_vote_comment_success_should_return_dto(){
        when(userRepository.getById(any())).thenReturn(user);
        when(commentRepository.getById(any())).thenReturn(comment);
        when(voteCommentRepository.save(any())).thenReturn(voteComment);

        VoteCommentDTO voteCommentDTO = voteCommentService.createVoteComment(this.voteCommentDTO);

        assertNotNull(voteCommentDTO);
        assertEquals(voteCommentDTO.getComment_id(), this.voteCommentDTO.getComment_id());
        assertEquals(voteCommentDTO.getUser_id(), this.voteCommentDTO.getUser_id());
        assertEquals(voteCommentDTO.getVote(), this.voteCommentDTO.getVote());
    }

    @Test
    void when_vote_not_found_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(user);
        when(commentRepository.getById(any())).thenReturn(comment);
        when(voteCommentRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            voteCommentService.updateVoteComment(voteCommentDTO);
        });
    }

    @Test
    void when_update_vote_error_should_throw_error(){
        voteCommentDTO.setVote(false);
        when(userRepository.getById(any())).thenReturn(user);
        when(commentRepository.getById(any())).thenReturn(comment);
        when(voteCommentRepository.findById(any())).thenReturn(Optional.of(voteComment));
        when(voteCommentRepository.save(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            voteCommentService.updateVoteComment(voteCommentDTO);
        });
    }

    @Test
    void when_update_vote_success_should_have_no_errors(){
        voteCommentDTO.setVote(false);
        when(userRepository.getById(any())).thenReturn(user);
        when(commentRepository.getById(any())).thenReturn(comment);
        when(voteCommentRepository.findById(any())).thenReturn(Optional.of(voteComment));
        when(voteCommentRepository.save(any())).thenReturn(voteComment);

        assertDoesNotThrow(() -> {
            voteCommentService.updateVoteComment(voteCommentDTO);
        });
    }

    @Test
    void when_delete_vote_error_should_throw_error(){
        when(userRepository.getById(any())).thenReturn(user);
        when(commentRepository.getById(any())).thenReturn(comment);
        when(voteCommentRepository.findById(any())).thenReturn(Optional.of(voteComment));
        doThrow(IllegalArgumentException.class).when(voteCommentRepository).delete(any());

        assertThrows(RuntimeException.class, () -> {
            voteCommentService.deleteVoteComment(voteCommentDTO);
        });
    }

    @Test
    void when_delete_vote_success_should_have_no_errors(){
        when(userRepository.getById(any())).thenReturn(user);
        when(commentRepository.getById(any())).thenReturn(comment);
        when(voteCommentRepository.findById(any())).thenReturn(Optional.of(voteComment));

        assertDoesNotThrow(() -> {
            voteCommentService.deleteVoteComment(voteCommentDTO);
        });
    }
}
