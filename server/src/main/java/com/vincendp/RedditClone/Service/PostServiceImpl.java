package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.Post;
import com.vincendp.RedditClone.Model.PostType;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.PostTypeRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostServiceImpl implements PostService{

    private PostRepository postRepository;

    private UserRepository userRepository;

    private SubredditRepository subredditRepository;

    private PostTypeRepository postTypeRepository;

    private StorageService storageService;


    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           UserRepository userRepository,
                           SubredditRepository subredditRepository,
                           PostTypeRepository postTypeRepository,
                           StorageService storageService){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.subredditRepository = subredditRepository;
        this.postTypeRepository = postTypeRepository;
    }

    @Override
    public CreatePostResponse createPost(CreatePostRequest createPostRequest){
        Subreddit subreddit = null;
        User user = null;

        try{
            UUID subreddit_uuid = UUID.fromString(createPostRequest.getSubreddit_id());
            UUID user_uuid = UUID.fromString(createPostRequest.getUser_id());
            subreddit = subredditRepository.getById(subreddit_uuid);
            user = userRepository.getById(user_uuid);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Error: Invalid user or subreddit");
        }

        if(user == null || subreddit == null){
            throw new ResourceNotFoundException("Error: User or subreddit not found");
        }


        PostType postType = postTypeRepository.findById(createPostRequest.getPost_type()).get();
        Post post = new Post(null, createPostRequest.getTitle(), null, user, subreddit, postType);
        post.setDescription(createPostRequest.getDescription());
        post.setLink(createPostRequest.getLink());
        post.setImage_path("/");

        post = postRepository.save(post);

        return new CreatePostResponse(post.getId().toString(), post.getTitle(), post.getDescription(),
                post.getLink(), post.getUser().getId().toString(), post.getSubreddit().getId().toString(),
                post.getCreated_at());
    }
}
