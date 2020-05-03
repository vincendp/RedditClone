package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.VotePostDTO;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.Post;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.VotePost;
import com.vincendp.RedditClone.Model.VotePostId;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Repository.VotePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VotePostServiceImpl implements VotePostService{

    private PostRepository postRepository;

    private UserRepository userRepository;

    private VotePostRepository votePostRepository;

    @Autowired
    public VotePostServiceImpl(VotePostRepository votePostRepository
            , PostRepository postRepository, UserRepository userRepository){
        this.votePostRepository = votePostRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public VotePostDTO createVotePost(VotePostDTO votePostDTO) {
        User user = getUser(votePostDTO.getUser_id());
        Post post = getPost(votePostDTO.getPost_id());

        VotePostId votePostId = new VotePostId(user, post);
        VotePost votePost = new VotePost(votePostId, votePostDTO.getVote());

        try{
            votePost = votePostRepository.save(votePost);
        }
        catch(Exception e){
            throw new RuntimeException("Error: Could not save vote");
        }

        return new VotePostDTO(votePost.getVotePostId().getUser().getId().toString(),
                votePost.getVotePostId().getPost().getId().toString(), votePost.getVote() );
    }

    @Override
    public void updateVotePost(VotePostDTO votePostDTO) {
        User user = getUser(votePostDTO.getUser_id());
        Post post = getPost(votePostDTO.getPost_id());
        VotePost votePost = getVotePost(user, post);

        votePost.setVote(votePostDTO.getVote());

        try {
            votePost = votePostRepository.save(votePost);
        }
        catch(Exception e){
            throw new RuntimeException("Error: Could not update vote");
        }
    }

    @Override
    public void deleteVotePost(VotePostDTO votePostDTO) {
        User user = getUser(votePostDTO.getUser_id());
        Post post = getPost(votePostDTO.getPost_id());
        VotePost votePost = getVotePost(user, post);

        try{
            votePostRepository.delete(votePost);
        }
        catch(Exception e){
            throw new RuntimeException("Error: Could not delete vote");
        }
    }

    private User getUser(String user_id){
        User user = null;

        try{
            UUID user_uuid = UUID.fromString(user_id);
            user = userRepository.getById(user_uuid);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Error: Invalid user");
        }

        if(user == null){
            throw new ResourceNotFoundException("Error: User not found");
        }

        return user;
    }

    private Post getPost(String post_id){
        Post post = null;

        try{
            UUID post_uuid = UUID.fromString(post_id);
            post = postRepository.getById(post_uuid);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Error: Invalid post");
        }

        if(post == null){
            throw new ResourceNotFoundException("Error: Post not found");
        }

        return post;
    }

    private VotePost getVotePost(User user, Post post){
        VotePostId votePostId = new VotePostId(user, post);
        VotePost votePost = null;

        try{
            votePost = votePostRepository.findById(votePostId).get();
        }
        catch(Exception e){
            throw new ResourceNotFoundException("Error: Could not find vote");
        }

        return votePost;
    }
}
