package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.Post;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService{

    private PostRepository postRepository;

    private UserRepository userRepository;

    private SubredditRepository subredditRepository;


    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           UserRepository userRepository,
                           SubredditRepository subredditRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.subredditRepository = subredditRepository;
    }

    @Override
    public CreatePostResponse createPost(CreatePostRequest createPostRequest){
        Subreddit subreddit = null;
        User user = null;

        try{
            subreddit = subredditRepository.findById(UUID.fromString(createPostRequest.getSubreddit_id())).get();
            user = userRepository.findById(UUID.fromString(createPostRequest.getUser_id())).get();
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Error: Invalid user or subreddit");
        }
        catch(NoSuchElementException e){
            throw new ResourceNotFoundException("Error: User or subreddit not found");
        }

        Post post = new Post();
        post.setTitle(createPostRequest.getTitle());
        post.setDescription(createPostRequest.getDescription());
        post.setLink(createPostRequest.getLink());
        post.setUser(user);
        post.setSubreddit(subreddit);

        postRepository.save(post);

        return new CreatePostResponse(post.getId().toString(), post.getTitle(), post.getDescription(),
                post.getLink(), post.getUser().getId().toString(), post.getSubreddit().getId().toString(),
                post.getCreated_at());
    }
}
