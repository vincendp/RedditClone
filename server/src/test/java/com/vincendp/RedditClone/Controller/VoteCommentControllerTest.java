package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Config.TestSecurityConfiguration;
import com.vincendp.RedditClone.Dto.VoteCommentDTO;
import com.vincendp.RedditClone.Service.VoteCommentService;
import com.vincendp.RedditClone.Service.VotePostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { TestSecurityConfiguration.class, VoteController.class })
public class VoteCommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotePostService votePostService;

    @MockBean
    private VoteCommentService voteCommentService;

    private VoteCommentDTO voteCommentDTO;

    @BeforeEach
    void init(){
        voteCommentDTO = new VoteCommentDTO(UUID.randomUUID().toString(), UUID.randomUUID().toString(), true);
    }

    @Test
    void when_vote_comment_user_empty_should_throw_error() throws Exception{
        voteCommentDTO.setUser_id(null);
        String json = objectMapper.writeValueAsString(voteCommentDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/votes/comments")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_vote_comment_comment_empty_should_throw_error() throws Exception{
        voteCommentDTO.setComment_id(null);
        String json = objectMapper.writeValueAsString(voteCommentDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/votes/comments")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_vote_comment_vote_empty_should_throw_error() throws Exception{
        voteCommentDTO.setVote(null);
        String json = objectMapper.writeValueAsString(voteCommentDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/votes/comments")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_create_vote_comment_service_error_should_throw_error() throws Exception{
        when(voteCommentService.createVoteComment(any(VoteCommentDTO.class))).thenThrow(RuntimeException.class);
        String json = objectMapper.writeValueAsString(voteCommentDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/votes/comments")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_create_vote_comment_success_should_return_ok() throws Exception{
        VoteCommentDTO voteCommentDTO = new VoteCommentDTO(
                this.voteCommentDTO.getUser_id(), this.voteCommentDTO.getComment_id(), this.voteCommentDTO.getVote());
        when(voteCommentService.createVoteComment(any(VoteCommentDTO.class))).thenReturn(voteCommentDTO);
        String json = objectMapper.writeValueAsString(this.voteCommentDTO);

        mockMvc.perform(post("/votes/comments")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created vote")));
    }

    @Test
    void when_update_vote_comment_service_error_should_throw_error() throws Exception{
        doThrow(RuntimeException.class).when(voteCommentService).updateVoteComment(any());
        String json = objectMapper.writeValueAsString(voteCommentDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(put("/votes/comments")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_update_vote_comment_success_should_return_ok() throws Exception{
        String json = objectMapper.writeValueAsString(voteCommentDTO);

        mockMvc.perform(put("/votes/comments")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Updated vote")));
    }

    @Test
    void when_delete_vote_comment_service_error_should_throw_error() throws Exception{
        doThrow(RuntimeException.class).when(voteCommentService).deleteVoteComment(any());
        String json = objectMapper.writeValueAsString(voteCommentDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(delete("/votes/comments")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_delete_vote_comment_success_should_return_ok() throws Exception{
        String json = objectMapper.writeValueAsString(voteCommentDTO);

        mockMvc.perform(delete("/votes/comments")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Deleted vote")));
    }

}
