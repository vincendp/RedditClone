package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Dto.GetPostDTO;
import com.vincendp.RedditClone.Dto.GetPostPreviewDTO;

import java.util.List;

public interface PostService {

    GetPostDTO getPost(String post_id);

    List<GetPostPreviewDTO> getAllPostPreviews();

    List<GetPostPreviewDTO> getAllPostPreviewsByUser(String user_id);

    List<GetPostPreviewDTO> getAllPostPreviewsBySubreddit(String subreddit_id);

    CreatePostResponse createPost(CreatePostRequest createPostRequest);


}
