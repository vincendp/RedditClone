package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Dto.VotePostDTO;
import com.vincendp.RedditClone.Model.Post;
import com.vincendp.RedditClone.Model.PostType;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql", "/sql/data.sql"})
public class VotePostControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    private VotePostDTO votePostDTO;

    @BeforeEach
    void init(){
        User user = new User(null, "bob", new Date());
        Subreddit subreddit = new Subreddit(null, "subreddit", new Date());

        userRepository.save(user);
        subredditRepository.save(subreddit);

        Post post = new Post(null, "title", new Date(), user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString()));

        postRepository.save(post);

        votePostDTO = new VotePostDTO(user.getId().toString(), post.getId().toString(), true);
    }

    @Test
    void when_create_post_success_returns_response_ok() throws Exception{
        String json = objectMapper.writeValueAsString(votePostDTO);

        MvcResult result = mockMvc.perform(post("/votes/posts")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        VotePostDTO votePostDTO = objectMapper.convertValue(successResponse.getResult(), VotePostDTO.class);
        assertNotNull(votePostDTO);
        assertEquals(votePostDTO.getPost_id(), this.votePostDTO.getPost_id());
        assertEquals(votePostDTO.getUser_id(), this.votePostDTO.getUser_id());
    }

    @Test
    void when_update_post_success_returns_response_ok() throws Exception{
        String json = objectMapper.writeValueAsString(votePostDTO);
        mockMvc.perform(post("/votes/posts")
                .header("Content-Type", "application/json")
                .content(json));

        votePostDTO.setVote(false);
        json = objectMapper.writeValueAsString(votePostDTO);

        mockMvc.perform(put("/votes/posts")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Updated vote")));
    }

    @Test
    void when_delete_post_success_returns_response_ok() throws Exception{
        String json = objectMapper.writeValueAsString(votePostDTO);
        mockMvc.perform(post("/votes/posts")
                .header("Content-Type", "application/json")
                .content(json));

        mockMvc.perform(delete("/votes/posts")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Deleted vote")));
    }
}
