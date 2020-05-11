package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateCommentRequest;
import com.vincendp.RedditClone.Dto.CreateCommentResponse;
import com.vincendp.RedditClone.Dto.GetCommentDTO;

import java.util.List;

public interface CommentService {

    CreateCommentResponse createComment(CreateCommentRequest createCommentRequest);

    List<GetCommentDTO> getCommentsFromPost(String post_id);
}
