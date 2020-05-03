package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Config.TestSecurityConfiguration;
import com.vincendp.RedditClone.Dto.VotePostDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest(VoteController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { TestSecurityConfiguration.class, VoteController.class })
public class VotePostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotePostService votePostService;

    @MockBean
    private VoteCommentService voteCommentService;

    private VotePostDTO votePostDTO;

    @BeforeEach
    void init(){
        votePostDTO = new VotePostDTO(UUID.randomUUID().toString(), UUID.randomUUID().toString(), true);
    }

    @Test
    void when_vote_post_user_empty_should_throw_error() throws Exception{
        votePostDTO.setUser_id(null);
        String json = objectMapper.writeValueAsString(votePostDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/votes/posts")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_vote_post_post_empty_should_throw_error() throws Exception{
        votePostDTO.setPost_id(null);
        String json = objectMapper.writeValueAsString(votePostDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/votes/posts")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_vote_post_vote_empty_should_throw_error() throws Exception{
        votePostDTO.setVote(null);
        String json = objectMapper.writeValueAsString(votePostDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/votes/posts")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_create_vote_post_service_error_should_throw_error() throws Exception{
        when(votePostService.createVotePost(any(VotePostDTO.class))).thenThrow(RuntimeException.class);
        String json = objectMapper.writeValueAsString(votePostDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/votes/posts")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_create_vote_post_success_should_return_ok() throws Exception{
        VotePostDTO votePostDTO = new VotePostDTO(
                this.votePostDTO.getUser_id(), this.votePostDTO.getPost_id(), this.votePostDTO.getVote());
        when(votePostService.createVotePost(any(VotePostDTO.class))).thenReturn(votePostDTO);
        String json = objectMapper.writeValueAsString(this.votePostDTO);

        mockMvc.perform(post("/votes/posts")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created vote")));
    }

    @Test
    void when_update_vote_post_service_error_should_throw_error() throws Exception{
        doThrow(RuntimeException.class).when(votePostService).updateVotePost(any());
        String json = objectMapper.writeValueAsString(votePostDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(put("/votes/posts")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_update_vote_post_success_should_return_ok() throws Exception{
        String json = objectMapper.writeValueAsString(votePostDTO);

        mockMvc.perform(put("/votes/posts")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Updated vote")));
    }

    @Test
    void when_delete_vote_post_service_error_should_throw_error() throws Exception{
        doThrow(RuntimeException.class).when(votePostService).deleteVotePost(any());
        String json = objectMapper.writeValueAsString(votePostDTO);

        assertThatThrownBy(() -> {
            mockMvc.perform(delete("/votes/posts")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_delete_vote_post_success_should_return_ok() throws Exception{
        String json = objectMapper.writeValueAsString(votePostDTO);

        mockMvc.perform(delete("/votes/posts")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Deleted vote")));
    }
}
