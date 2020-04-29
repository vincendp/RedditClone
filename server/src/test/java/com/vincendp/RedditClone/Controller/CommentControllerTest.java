package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Config.TestSecurityConfiguration;
import com.vincendp.RedditClone.Dto.CreateCommentRequest;
import com.vincendp.RedditClone.Dto.CreateCommentResponse;
import com.vincendp.RedditClone.Service.CommentService;
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

import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest(CommentController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { TestSecurityConfiguration.class, CommentController.class })
public class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private CreateCommentRequest createCommentRequest;

    @BeforeEach
    void init(){
        createCommentRequest = new CreateCommentRequest(
                "This is a comment", UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    @Test
    void when_comment_is_empty_should_throw_error() throws Exception{
        createCommentRequest.setComment(null);
        String json = objectMapper.writeValueAsString(createCommentRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/comments")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_user_id_is_empty_should_throw_error() throws Exception{
        createCommentRequest.setUser_id(null);
        String json = objectMapper.writeValueAsString(createCommentRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/comments")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_post_id_is_empty_should_throw_error() throws Exception{
        createCommentRequest.setPost_id(null);
        String json = objectMapper.writeValueAsString(createCommentRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/comments")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_comment_service_error_should_throw_error() throws Exception{
        String json = objectMapper.writeValueAsString(createCommentRequest);
        when(commentService.createComment(any(CreateCommentRequest.class))).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/comments")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_valid_comment_should_return_response_ok() throws Exception{
        String json = objectMapper.writeValueAsString(createCommentRequest);
        CreateCommentResponse createCommentResponse
                = new CreateCommentResponse(UUID.randomUUID().toString()
                , createCommentRequest.getComment(), createCommentRequest.getUser_id()
                , createCommentRequest.getPost_id(), new Date());
        when(commentService.createComment(any(CreateCommentRequest.class))).thenReturn(createCommentResponse);

        mockMvc.perform(post("/comments")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created comment")));
    }
}
