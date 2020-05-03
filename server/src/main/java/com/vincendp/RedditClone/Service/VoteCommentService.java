package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.VoteCommentDTO;

public interface VoteCommentService {

    VoteCommentDTO createVoteComment(VoteCommentDTO voteCommentDTO);

    void updateVoteComment(VoteCommentDTO voteCommentDTO);

    void deleteVoteComment(VoteCommentDTO voteCommentDTO);
}
