package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateSubredditRequest;
import com.vincendp.RedditClone.Dto.CreateSubredditResponse;
import com.vincendp.RedditClone.Dto.GetSubredditResponse;
import com.vincendp.RedditClone.Exception.ResourceAlreadyExistsException;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubredditServiceTest {

    @InjectMocks
    private SubredditServiceImpl subredditService;

    @Mock
    private SubredditRepository subredditRepository;

    private CreateSubredditRequest createSubredditRequest;

    @BeforeEach
    void setup(){
        createSubredditRequest = new CreateSubredditRequest("subreddit");
    }

    @Test
    void when_existing_subreddit_should_throw_error(){
        when(subredditRepository.findByName(anyString())).thenReturn(new Subreddit());

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            subredditService.createSubreddit(createSubredditRequest);
        });
    }

    @Test
    void when_repository_throws_error_should_throw_error(){
        when(subredditRepository.findByName(anyString())).thenReturn(null);
        when(subredditRepository.save(any())).thenThrow(DataIntegrityViolationException.class).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            subredditService.createSubreddit(createSubredditRequest);
        });
    }

    @Test
    void when_valid_subreddit_should_return_response(){
        Subreddit subreddit = new Subreddit(
                UUID.randomUUID(), createSubredditRequest.getName(), new Date()
        );
        when(subredditRepository.findByName(anyString())).thenReturn(null);
        when(subredditRepository.save(any())).thenReturn(subreddit);

        CreateSubredditResponse createSubredditResponse = subredditService.createSubreddit(createSubredditRequest);

        assertNotNull(createSubredditResponse);
        assertNotNull(createSubredditResponse.getId());
        assertNotNull(createSubredditResponse.getCreated_at());
        assertEquals(createSubredditRequest.getName(), createSubredditResponse.getName());
    }

    @Test
    void when_empty_subreddits_then_return_empty_list(){
        when(subredditRepository.findAll()).thenReturn(new ArrayList<>());
        List<GetSubredditResponse> subreddits = subredditService.getSubreddits();
        assertNotNull(subreddits);
        assertTrue(subreddits.size() == 0);
    }

    @Test
    void when_has_subreddits_then_return_list(){
        when(subredditRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(
                new Subreddit(UUID.randomUUID(), "subreddit1", new Date()),
                new Subreddit(UUID.randomUUID(), "subreddit2", new Date()),
                new Subreddit(UUID.randomUUID(), "subreddit3", new Date())
        )));
        List<GetSubredditResponse> subreddits = subredditService.getSubreddits();
        assertNotNull(subreddits);
        assertTrue(subreddits.size() > 0);
    }

}
