package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;

public interface PostService {

    CreatePostResponse createPost(CreatePostRequest createPostRequest);

}
