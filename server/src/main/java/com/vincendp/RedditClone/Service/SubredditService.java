package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateSubredditRequest;
import com.vincendp.RedditClone.Dto.CreateSubredditResponse;

public interface SubredditService {

    CreateSubredditResponse createSubreddit(CreateSubredditRequest createSubredditRequest);

}
