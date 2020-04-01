package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateSubredditRequest;
import com.vincendp.RedditClone.Dto.CreateSubredditResponse;
import com.vincendp.RedditClone.Dto.GetSubredditResponse;

import java.util.List;

public interface SubredditService {

    CreateSubredditResponse createSubreddit(CreateSubredditRequest createSubredditRequest);
    List<GetSubredditResponse> getSubreddits();

}
