package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Service.PostService;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("posts")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping
    ResponseEntity createPost(@RequestBody CreatePostRequest createPostRequest){
        if( createPostRequest.getTitle() == null
                || createPostRequest.getTitle().length() <= 0){
            throw new IllegalArgumentException("Error: title cannot be empty.");
        }
        else if (createPostRequest.getUser_id() == null
                || createPostRequest.getSubreddit_id() == null
                || createPostRequest.getUser_id().length() <= 0
                || createPostRequest.getSubreddit_id().length() <= 0){
            throw new IllegalArgumentException("Error: user or subreddit cannot be empty.");
        }

        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);

        return ResponseEntity.ok(new SuccessResponse(200, "Success: Created post", createPostRequest));
    }

}
