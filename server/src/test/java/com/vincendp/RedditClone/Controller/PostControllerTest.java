package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vincendp.RedditClone.Config.TestSecurityConfiguration;
import com.vincendp.RedditClone.Dto.*;
import com.vincendp.RedditClone.Model.PostType;
import com.vincendp.RedditClone.Service.PostService;
import com.vincendp.RedditClone.Utility.SuccessResponse;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PostController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { TestSecurityConfiguration.class, PostController.class })
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    private MultiValueMap<String, String> params;

    private CreatePostResponse createPostResponse;

    @BeforeEach
    void setup(){
        params = new LinkedMultiValueMap<>();
        params.add("title", "title");
        params.add("description", "description");
        params.add("link", "https://www.google.com");
        params.add("user_id", UUID.randomUUID().toString());
        params.add("subreddit_id", UUID.randomUUID().toString());
        params.add("post_type", PostType.Type.TEXT.getValue() + "");

        createPostResponse = new CreatePostResponse(
                UUID.randomUUID().toString(), params.getFirst("title"),
                params.getFirst("description"), params.getFirst("link"),
                params.getFirst("/"), params.getFirst("user_id"),
                params.getFirst("subreddit_id"), new Date()
        );
    }

    @Test
    void when_title_is_empty_should_throw_error(){
        params.set("title", null);
        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_user_id_is_empty_should_throw_error(){
        params.set("user_id", null);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_subreddit_id_is_empty_should_throw_error(){
        params.set("subreddit_id", null);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_post_type_is_empty_should_throw_error(){
        params.set("post_type", null);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_link_post_and_link_is_empty_should_throw_error(){
        params.set("post_type", PostType.Type.LINK.getValue() + "");
        params.set("link", null);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_image_post_and_image_is_empty_should_throw_error(){
        params.set("post_type", PostType.Type.IMAGE.getValue() + "");

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .file(new MockMultipartFile("images", "image1", "image/png", new byte[0]))
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_image_post_and_unsupported_format_should_throw_error(){
        params.set("post_type", PostType.Type.IMAGE.getValue() + "");

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .file(new MockMultipartFile("images", "image1", "image/gif", "image1".getBytes()))
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_post_type_invalid_should_throw_error(){
        params.set("post_type", "-1");

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .params(params));
        }).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_create_post_throws_error_should_throw_error(){
        when(postService.createPost(any(CreatePostRequest.class))).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> {
            mockMvc.perform(multipart("/posts")
                    .params(params));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_create_text_post_success_should_return_response_success() throws Exception {
        when(postService.createPost(any(CreatePostRequest.class))).thenReturn(createPostResponse);
        mockMvc.perform(multipart("/posts")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created post")));
    }

    @Test
    void when_create_link_post_success_should_return_response_success() throws Exception{
        when(postService.createPost(any(CreatePostRequest.class))).thenReturn(createPostResponse);
        params.set("post_type", PostType.Type.LINK.getValue() + "");
        mockMvc.perform(multipart("/posts")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created post")));
    }

    @Test
    void when_create_image_post_success_should_return_response_success() throws Exception{
        when(postService.createPost(any(CreatePostRequest.class))).thenReturn(createPostResponse);
        params.set("post_type", PostType.Type.IMAGE.getValue() + "");
        mockMvc.perform(multipart("/posts")
                .file(new MockMultipartFile("image", "image1.png", "image/png", "image1".getBytes()))
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created post")));

        mockMvc.perform(multipart("/posts")
                .file(new MockMultipartFile("image", "image1.jpg", "image/jpg", "image1".getBytes()))
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created post")));

        mockMvc.perform(multipart("/posts")
                .file(new MockMultipartFile("image", "image1.jpeg", "image/jpeg", "image1".getBytes()))
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created post")));
    }

    @Test
    void when_get_post_service_throws_error_should_throw_error(){
        when(postService.getPost(anyString())).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> {
            mockMvc.perform(get("/posts/{post_id}", UUID.randomUUID().toString())
                    .header("Content-Type", "application/json"));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_get_post_success_should_return_response_ok() throws Exception{
        when(postService.getPost(anyString())).thenReturn(new GetPostDTO());
        mockMvc.perform(get("/posts/{post_id}", UUID.randomUUID().toString())
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Got post")));
    }

    @Test
    void when_get_all_post_previews_and_service_throws_error_should_throw_error(){
        when(postService.getAllPostPreviews()).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> {
            mockMvc.perform(get("/posts")
                    .header("Content-Type", "application/json"));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_get_all_post_previews_and_no_post_previews_should_return_response_with_empty_list() throws Exception{
        when(postService.getAllPostPreviews()).thenReturn(new ArrayList<>());
        MvcResult result = mockMvc.perform(get("/posts")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, GetPostPreviewDTO.class);
        List<GetPostPreviewDTO> postPreviews = objectMapper.convertValue(successResponse.getResult(), typeReference);
        assertNotNull(postPreviews);
        assertThat(postPreviews.isEmpty());
    }

    @Test
    void when_get_all_post_previews_and_has_posts_should_return_response_with_list() throws Exception{
        when(postService.getAllPostPreviews()).thenReturn(new ArrayList<>(
                Arrays.asList(new GetPostPreviewDTO(), new GetPostPreviewDTO(), new GetPostPreviewDTO())
        ));
        MvcResult result = mockMvc.perform(get("/posts")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, GetPostPreviewDTO.class);
        List<GetPostPreviewDTO> postPreviews = objectMapper.convertValue(successResponse.getResult(), typeReference);
        assertNotNull(postPreviews);
        assertEquals(3, postPreviews.size());
    }

    @Test
    void when_get_all_post_previews_by_specific_user_and_service_throws_error_should_throw_error(){
        when(postService.getAllPostPreviewsByUser(anyString())).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> {
            mockMvc.perform(get("/posts/users/{user_id}", UUID.randomUUID().toString())
                    .header("Content-Type", "application/json"));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_get_all_post_previews_by_specific_user_and_no_post_previews_should_return_response_with_empty_list() throws Exception{
        when(postService.getAllPostPreviewsByUser(anyString())).thenReturn(new ArrayList<>());
        MvcResult result = mockMvc.perform(get("/posts/users/{user_id}", UUID.randomUUID().toString())
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, GetPostPreviewDTO.class);
        List<GetPostPreviewDTO> postPreviews = objectMapper.convertValue(successResponse.getResult(), typeReference);
        assertNotNull(postPreviews);
        assertThat(postPreviews.isEmpty());
    }

    @Test
    void when_get_all_post_previews_by_specific_user_and_has_posts_should_return_response_with_list() throws Exception{
        when(postService.getAllPostPreviewsByUser(anyString())).thenReturn(new ArrayList<>(
                Arrays.asList(new GetPostPreviewDTO(), new GetPostPreviewDTO())
        ));
        MvcResult result = mockMvc.perform(get("/posts/users/{user_id}", UUID.randomUUID().toString())
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, GetPostPreviewDTO.class);
        List<GetPostPreviewDTO> postPreviews = objectMapper.convertValue(successResponse.getResult(), typeReference);
        assertNotNull(postPreviews);
        assertEquals(2, postPreviews.size());
    }

    @Test
    void when_get_all_post_previews_by_specific_subreddit_and_service_throws_error_should_throw_error(){
        when(postService.getAllPostPreviewsBySubreddit(anyString())).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> {
            mockMvc.perform(get("/posts/subreddits/{subreddit_id}", UUID.randomUUID().toString())
                    .header("Content-Type", "application/json"));
        }).hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void when_get_all_post_previews_by_specific_subreddit_and_no_post_previews_should_return_response_with_empty_list() throws  Exception{
        when(postService.getAllPostPreviewsBySubreddit(anyString())).thenReturn(new ArrayList<>());
        MvcResult result = mockMvc.perform(get("/posts/subreddits/{subreddit_id}", UUID.randomUUID().toString())
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, GetPostPreviewDTO.class);
        List<GetPostPreviewDTO> postPreviews = objectMapper.convertValue(successResponse.getResult(), typeReference);
        assertNotNull(postPreviews);
        assertThat(postPreviews.isEmpty());
    }

    @Test
    void when_get_all_post_previews_by_specific_subreddit_and_has_posts_should_return_response_with_list() throws Exception{
        when(postService.getAllPostPreviewsBySubreddit(anyString())).thenReturn(new ArrayList<>(
                Arrays.asList(new GetPostPreviewDTO())
        ));
        MvcResult result = mockMvc.perform(get("/posts/subreddits/{subreddit_id}", UUID.randomUUID().toString())
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, GetPostPreviewDTO.class);
        List<GetPostPreviewDTO> postPreviews = objectMapper.convertValue(successResponse.getResult(), typeReference);
        assertNotNull(postPreviews);
        assertEquals(1, postPreviews.size());
    }
}
