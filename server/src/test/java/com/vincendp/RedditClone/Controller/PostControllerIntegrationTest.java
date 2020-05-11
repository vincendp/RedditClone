package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Model.Post;
import com.vincendp.RedditClone.Model.PostType;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.PostTypeRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Service.PostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql", "/sql/data.sql"})
public class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostTypeRepository postTypeRepository;

    private Subreddit subreddit;

    private User user;

    private Post post;

    private CreatePostRequest createPostRequest;

    private MultiValueMap<String, String> params;

    private MockMultipartFile file;

    @BeforeEach
    void setup(){
        subreddit = new Subreddit(null, "subreddit", new Date());
        user = new User(null, "bob", new Date());

        subredditRepository.save(subreddit);
        userRepository.save(user);

        PostType postType = postTypeRepository.findById(PostType.Type.TEXT.getValue()).get();
        post = postRepository.save(new Post(null, "title", new Date(), user, subreddit, postType));

        params = new LinkedMultiValueMap<>();
        params.add("title", "title");
        params.add("description", "description");
        params.add("link", "https://www.google.com");
        params.add("user_id", user.getId().toString());
        params.add("subreddit_id", subreddit.getId().toString());
        params.add("post_type", PostType.Type.TEXT.getValue() + "");
    }

    @Test
    @WithMockUser("mockUser")
    void when_invalid_user_should_have_status_4xx() throws Exception{
        params.set("user_id", UUID.randomUUID().toString());
        mockMvc.perform(multipart("/posts")
                .params(params))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser("mockUser")
    void when_invalid_subreddit_should_have_status_4xx() throws Exception{
        params.set("subreddit_id", UUID.randomUUID().toString());
        mockMvc.perform(multipart("/posts")
                .params(params))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser("mockUser")
    void when_link_post_and_link_invalid_should_throw_error() throws Exception{
        params.set("post_type", PostType.Type.LINK.getValue() + "");
        params.set("link", "hi");

        mockMvc.perform(multipart("/posts")
                .params(params))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser("mockUser")
    void when_create_text_post_success_should_return_response_success() throws Exception {
        mockMvc.perform(multipart("/posts")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created post")));
    }

    @Test
    @WithMockUser("mockUser")
    void when_create_link_post_success_should_return_response_success() throws Exception{
        params.set("post_type", PostType.Type.LINK.getValue() + "");
        mockMvc.perform(multipart("/posts")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created post")));
    }

    @Test
    @WithMockUser("mockUser")
    void when_create_image_post_success_should_return_response_success() throws Exception{
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
    void when_get_post_not_found_should_return_status_404() throws Exception{
        mockMvc.perform(get("/posts/{post_id}", UUID.randomUUID().toString()))
        .andExpect(status().isNotFound());
    }

    @Test
    void when_get_post_should_return_response_success() throws Exception{
        mockMvc.perform(get("/posts/{post_id}", post.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Got post")));
    }
}
