package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Model.PostType;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Service.PostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

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

    private Subreddit subreddit;

    private User user;

    private CreatePostRequest createPostRequest;

    @BeforeEach
    void setup(){
        subreddit = new Subreddit(null, "subreddit", new Date());
        user = new User(null, "bob", new Date());

        subredditRepository.save(subreddit);
        userRepository.save(user);

//        createPostRequest = new CreatePostRequest("title", "description"
//                , "https://www.google.com", user.getId().toString(), subreddit.getId().toString(), PostType.Type.TEXT.getValue());
    }

    @Test
    void when_invalid_user_should_have_status_4xx() throws Exception{
        createPostRequest.setUser_id(UUID.randomUUID().toString());
        String json = objectMapper.writeValueAsString(createPostRequest);

        mockMvc.perform(post("/posts")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void when_invalid_subreddit_should_have_status_4xx() throws Exception{
        createPostRequest.setSubreddit_id(UUID.randomUUID().toString());
        String json = objectMapper.writeValueAsString(createPostRequest);

        mockMvc.perform(post("/posts")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void when_create_post_valid_should_return_response_success() throws Exception{
        String json = objectMapper.writeValueAsString(createPostRequest);

        mockMvc.perform(post("/posts")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created post")));
    }
}
