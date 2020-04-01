package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateSubredditRequest;
import com.vincendp.RedditClone.Dto.CreateSubredditResponse;
import com.vincendp.RedditClone.Exception.ResourceAlreadyExistsException;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
public class SubredditServiceImpl implements SubredditService{

    private SubredditRepository subredditRepository;

    @Autowired
    public SubredditServiceImpl(SubredditRepository subredditRepository) {
        this.subredditRepository = subredditRepository;
    }

    @Override
    public CreateSubredditResponse createSubreddit(CreateSubredditRequest createSubredditRequest) {

        Subreddit existingSubreddit = subredditRepository.findByName(createSubredditRequest.getName());

        if(existingSubreddit != null){
            throw new ResourceAlreadyExistsException("Error: Subreddit already exists");
        }

        Subreddit subreddit = new Subreddit(createSubredditRequest.getName());

        try{
            subreddit = subredditRepository.save(subreddit);
        }
        catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Error: Could not save subreddit");
        }

        return new CreateSubredditResponse(subreddit.getId().toString(), subreddit.getName(), subreddit.getCreated_at());
    }
}
