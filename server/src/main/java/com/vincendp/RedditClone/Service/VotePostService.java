package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.VotePostDTO;

public interface VotePostService {

    VotePostDTO createVotePost(VotePostDTO votePostDTO);

    void updateVotePost(VotePostDTO votePostDTO);

    void deleteVotePost(VotePostDTO votePostDTO);

}
