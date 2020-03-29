package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateSubredditRequest;
import com.vincendp.RedditClone.Dto.CreateSubredditResponse;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubredditServiceImpl implements SubredditService{

    private SubredditRepository subredditRepository;

    @Autowired
    public SubredditServiceImpl(SubredditRepository subredditRepository) {
        this.subredditRepository = subredditRepository;
    }

    @Override
    public CreateSubredditResponse createSubreddit(CreateSubredditRequest createSubredditRequest) {
        Subreddit subreddit = new Subreddit(createSubredditRequest.getName());
        subredditRepository.save(subreddit);

        return new CreateSubredditResponse();
    }
}
