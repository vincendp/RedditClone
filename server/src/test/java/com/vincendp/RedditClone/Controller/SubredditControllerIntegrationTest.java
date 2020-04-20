package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Dto.CreateSubredditRequest;
import com.vincendp.RedditClone.Dto.GetSubredditResponse;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Service.SubredditService;
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

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql"})
public class SubredditControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubredditService subredditService;

    @Autowired
    private SubredditRepository subredditRepository;

    private CreateSubredditRequest createSubredditRequest;

    @BeforeEach
    void setup(){
        createSubredditRequest = new CreateSubredditRequest("subreddit");
    }

    @Test
    void when_service_throws_error_should_have_status_4xx() throws Exception{
        subredditRepository.save(new Subreddit(null, "subreddit", new Date()));
        String json = objectMapper.writeValueAsString(createSubredditRequest);

        mockMvc.perform(post("/subreddits")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().is4xxClientError());

    }

    @Test
    void when_valid_subreddit_should_return_response_success() throws Exception{
        String json = objectMapper.writeValueAsString(createSubredditRequest);

        mockMvc.perform(post("/subreddits")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created subreddit")));
    }

    @Test
    void when_getting_all_subreddits_returns_status_ok() throws  Exception{
        mockMvc.perform(get("/subreddits")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Got subreddits")));


        subredditRepository.save(new Subreddit("subreddit1"));
        subredditRepository.save(new Subreddit("subreddit2"));
        subredditRepository.save(new Subreddit("subreddit3"));

        MvcResult result = mockMvc.perform(get("/subreddits")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Got subreddits")))
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        ArrayList<GetSubredditResponse> subreddits = (ArrayList<GetSubredditResponse>) successResponse.getResult();
        assertThat(subreddits.size() > 0);
    }
}
