package com.vincendp.RedditClone.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Config.TestSecurityConfiguration;
import com.vincendp.RedditClone.Dto.CreateSubredditRequest;
import com.vincendp.RedditClone.Dto.GetSubredditResponse;
import com.vincendp.RedditClone.Service.SubredditService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest(SubredditController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { TestSecurityConfiguration.class, SubredditController.class })
public class SubredditControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubredditService subredditService;

    private CreateSubredditRequest createSubredditRequest;

    @BeforeEach
    void setup(){
        createSubredditRequest = new CreateSubredditRequest("subreddit");
    }

    @Test
    void when_subreddit_name_empty_should_throw_error() throws Exception{
        createSubredditRequest.setName(null);
        String json = objectMapper.writeValueAsString(createSubredditRequest);

        assertThatThrownBy(() -> {
                mockMvc.perform(post("/subreddits")
                        .header("Content-Type", "application/json")
                        .content(json));
        }).hasCauseInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void when_service_throws_error_should_throw_error() throws Exception{
        when(subredditService.createSubreddit(any(CreateSubredditRequest.class))).thenThrow(RuntimeException.class);
        String json = objectMapper.writeValueAsString(createSubredditRequest);

        assertThatThrownBy(() -> {
            mockMvc.perform(post("/subreddits")
                    .header("Content-Type", "application/json")
                    .content(json));
        }).hasCauseInstanceOf(RuntimeException.class);
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
    void when_getting_all_returns_status_ok(){
        when(subredditService.getSubreddits()).thenReturn(new ArrayList<>())
                .thenReturn(new ArrayList<>(Arrays.asList(
                        new GetSubredditResponse(UUID.randomUUID().toString(), "1", new Date()),
                        new GetSubredditResponse(UUID.randomUUID().toString(), "2", new Date()),
                        new GetSubredditResponse(UUID.randomUUID().toString(), "3", new Date())
                )));

        
    }

}
