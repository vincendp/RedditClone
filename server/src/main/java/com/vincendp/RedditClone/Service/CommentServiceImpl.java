package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateCommentRequest;
import com.vincendp.RedditClone.Dto.CreateCommentResponse;
import com.vincendp.RedditClone.Dto.GetCommentDTO;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.Comment;
import com.vincendp.RedditClone.Model.CustomUserDetails;
import com.vincendp.RedditClone.Model.Post;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.CommentRepository;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Utility.SecurityContextUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    private UserRepository userRepository;

    private SecurityContextUtility securityContextUtility;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
                              UserRepository userRepository, SecurityContextUtility securityContextUtility){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.securityContextUtility = securityContextUtility;
    }

    @Override
    public List<GetCommentDTO> getCommentsFromPost(String post_id) {
        Post post = null;
        UUID post_uuid = null;

        try{
            post_uuid = UUID.fromString(post_id);
            post = postRepository.getById(post_uuid);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Error: Invalid post");
        }

        if(post == null){
            throw new ResourceNotFoundException("Error: Post not found");
        }

        CustomUserDetails userDetails = securityContextUtility.getUserDetailsFromSecurityContext();
        List<GetCommentDTO> dtos;

        if(userDetails != null && userDetails.getId() != null){
            dtos = commentRepository.getCommentsFromPost(post_uuid, userDetails.getId());
        }
        else{
            dtos = commentRepository.getCommentsFromPost(post_uuid, null);
        }

        return dtos;
    }

    @Override
    public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest) {
        User user = null;
        Post post = null;

        try{
            UUID user_uuid = UUID.fromString(createCommentRequest.getUser_id());
            UUID post_uuid = UUID.fromString(createCommentRequest.getPost_id());
            user = userRepository.getById(user_uuid);
            post = postRepository.getById(post_uuid);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Error: Invalid user or post");
        }

        if(user == null || post == null){
            throw new ResourceNotFoundException("Error: User or post not found");
        }

        Comment comment = new Comment(null, createCommentRequest.getComment(),
                false, null, user, post);

        comment = commentRepository.save(comment);

        return new CreateCommentResponse(comment.getId().toString(), comment.getComment(),
                comment.getUser().getId().toString(), comment.getPost().getId().toString(), comment.getCreated_at());
    }
}
