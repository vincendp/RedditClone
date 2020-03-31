package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Config.TestSecurityConfiguration;
import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.hamcrest.Matchers;


import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest(PostController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { TestSecurityConfiguration.class, PostController.class })
public class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    private CreatePostRequest createPostRequest;

    @BeforeEach
    void setup(){
        createPostRequest = new CreatePostRequest("title", "description"
                , null, UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    @Test
    void when_title_is_empty_should_throw_error() throws Exception{
        createPostRequest.setTitle(null);
        String json = objectMapper.writeValueAsString(createPostRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/posts")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_user_id_is_empty_should_throw_error() throws Exception{
        createPostRequest.setUser_id(null);
        String json = objectMapper.writeValueAsString(createPostRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/posts")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_subreddit_id_is_empty_should_throw_error() throws Exception{
        createPostRequest.setSubreddit_id(null);
        String json = objectMapper.writeValueAsString(createPostRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/posts")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_create_post_throws_error_should_throw_error() throws Exception{
        String json = objectMapper.writeValueAsString(createPostRequest);
        when(postService.createPost(any(CreatePostRequest.class))).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/posts")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_create_post_success_should_return_response_success() throws Exception{
        String json = objectMapper.writeValueAsString(createPostRequest);
        CreatePostResponse createPostResponse = new CreatePostResponse(
                UUID.randomUUID().toString(), createPostRequest.getTitle(),
                createPostRequest.getDescription(), createPostRequest.getLink(),
                createPostRequest.getUser_id(), createPostRequest.getSubreddit_id(),
                new Date()
        );

        when(postService.createPost(any(CreatePostRequest.class))).thenReturn(createPostResponse);

        mockMvc.perform(post("/posts")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created post")));
    }
}
