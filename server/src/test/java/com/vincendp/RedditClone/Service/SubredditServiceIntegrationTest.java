package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateSubredditRequest;
import com.vincendp.RedditClone.Dto.CreateSubredditResponse;
import com.vincendp.RedditClone.Dto.GetSubredditResponse;
import com.vincendp.RedditClone.Exception.ResourceAlreadyExistsException;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql"})
public class SubredditServiceIntegrationTest {

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
    void when_subreddit_already_exist_should_throw_error(){
        subredditRepository.save(new Subreddit("subreddit"));
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            subredditService.createSubreddit(createSubredditRequest);
        });
    }

    @Test
    void when_other_db_violation_should_throw_error(){
        assertThrows(DataIntegrityViolationException.class, () -> {
            subredditService.createSubreddit(new CreateSubredditRequest());
        });
    }

    @Test
    void when_valid_subreddit_should_return_response(){
        CreateSubredditResponse createSubredditResponse = subredditService.createSubreddit(createSubredditRequest);
        assertNotNull(createSubredditResponse);
        assertNotNull(createSubredditResponse.getId());
        assertNotNull(createSubredditResponse.getCreated_at());
        assertEquals(createSubredditRequest.getName(), createSubredditResponse.getName());
    }

    @Test
    void when_empty_subreddits_return_empty_list(){
        List<GetSubredditResponse> subreddits = subredditService.getSubreddits();
        assertNotNull(subreddits);
        assertTrue(subreddits.size() == 0);
    }

    @Test
    void when_has_subreddits_then_return_list(){
        subredditRepository.save(new Subreddit("subreddit1"));
        subredditRepository.save(new Subreddit("subreddit2"));
        subredditRepository.save(new Subreddit("subreddit3"));

        List<GetSubredditResponse> subreddits = subredditService.getSubreddits();
        assertNotNull(subreddits);
        assertTrue(subreddits.size() > 0);
    }
}
