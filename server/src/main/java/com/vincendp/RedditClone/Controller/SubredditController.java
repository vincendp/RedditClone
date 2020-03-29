package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.CreateSubredditRequest;
import com.vincendp.RedditClone.Dto.CreateSubredditResponse;
import com.vincendp.RedditClone.Service.SubredditService;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("subreddit")
public class SubredditController {

    private SubredditService subredditService;

    @Autowired
    public SubredditController(SubredditService subredditService){

    }

    @PostMapping
    public ResponseEntity createSubreddit(@RequestBody CreateSubredditRequest createSubredditRequest){
        if(createSubredditRequest.getName() == null
            || createSubredditRequest.getName().length() <= 0){
            throw new IllegalArgumentException("Error: name cannot be empty");
        }

        CreateSubredditResponse createSubredditResponse = subredditService.createSubreddit(createSubredditRequest);
        return ResponseEntity.ok(new SuccessResponse(200, "Success: Created post", createSubredditRequest));
    }

}
