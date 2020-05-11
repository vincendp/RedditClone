package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Dto.GetPostDTO;

public interface PostService {

    GetPostDTO getPost(String post_id);

    CreatePostResponse createPost(CreatePostRequest createPostRequest);



}
