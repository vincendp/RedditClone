package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Config.TestSecurityConfiguration;
import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Model.PostType;
import com.vincendp.RedditClone.Service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.hamcrest.Matchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


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

    private MultiValueMap<String, String> params;

    private MockMultipartFile file;

    @BeforeEach
    void setup(){
        file = new MockMultipartFile("images", "image1", "image/png", "image1".getBytes());
        params = new LinkedMultiValueMap<>();
        params.add("title", "title");
        params.add("description", "description");
        params.add("link", "https://www.google.com");
        params.add("user_id", UUID.randomUUID().toString());
        params.add("subreddit_id", UUID.randomUUID().toString());
        params.add("post_type", PostType.Type.TEXT.getValue() + "");
    }

    @Test
    void when_title_is_empty_should_throw_error(){
        params.set("title", null);
        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .file(file)
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_user_id_is_empty_should_throw_error(){
        params.set("user_id", null);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .file(file)
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_subreddit_id_is_empty_should_throw_error() throws Exception{
        params.set("subreddit_id", null);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .file(file)
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_post_type_is_empty_should_throw_error() throws Exception{
        params.set("post_type", null);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .file(file)
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_link_post_and_link_is_empty_should_throw_error() throws Exception{
        params.set("post_type", PostType.Type.LINK.getValue() + "");
        params.set("link", null);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .file(file)
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_post_type_invalid_should_throw_error() throws Exception{
        params.set("post_type", "-1");

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .file(file)
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_create_post_throws_error_should_throw_error() throws Exception{
        when(postService.createPost(any(CreatePostRequest.class))).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .file(file)
                    .params(params));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_create_post_success_should_return_response_success() throws Exception{
        CreatePostResponse createPostResponse = new CreatePostResponse(
                UUID.randomUUID().toString(), params.getFirst("title"),
                params.getFirst("description"), params.getFirst("link"),
                params.getFirst("user_id"), params.getFirst("subreddit_id"),
                new Date()
        );

        when(postService.createPost(any(CreatePostRequest.class))).thenReturn(createPostResponse);

        mockMvc.perform(multipart("/posts")
                .file(file)
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created post")));
    }
}
