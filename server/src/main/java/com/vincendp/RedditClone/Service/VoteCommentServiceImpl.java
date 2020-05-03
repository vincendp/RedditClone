package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.VoteCommentDTO;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.CommentRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Repository.VoteCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VoteCommentServiceImpl implements VoteCommentService{

    private VoteCommentRepository voteCommentRepository;

    private UserRepository userRepository;

    private CommentRepository commentRepository;

    @Autowired
    public VoteCommentServiceImpl(VoteCommentRepository voteCommentRepository,
                                  UserRepository userRepository,
                                  CommentRepository commentRepository){
        this.voteCommentRepository = voteCommentRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public VoteCommentDTO createVoteComment(VoteCommentDTO voteCommentDTO) {
        User user = getUser(voteCommentDTO.getUser_id());
        Comment comment = getComment(voteCommentDTO.getComment_id());

        VoteCommentId voteCommentId = new VoteCommentId(user, comment);
        VoteComment voteComment = new VoteComment(voteCommentId, voteCommentDTO.getVote());

        try{
            voteComment = voteCommentRepository.save(voteComment);
        }
        catch(Exception e){
            throw new RuntimeException("Error: Could not save vote");
        }

        return new VoteCommentDTO(voteComment.getVoteCommentId().getUser().getId().toString(),
                voteComment.getVoteCommentId().getComment().getId().toString(), voteComment.getVote() );
    }

    @Override
    public void updateVoteComment(VoteCommentDTO voteCommentDTO) {
        User user = getUser(voteCommentDTO.getUser_id());
        Comment comment = getComment(voteCommentDTO.getComment_id());
        VoteComment voteComment = getVoteComment(user, comment);

        voteComment.setVote(voteCommentDTO.getVote());

        try {
            voteComment = voteCommentRepository.save(voteComment);
        }
        catch(Exception e){
            throw new RuntimeException("Error: Could not update vote");
        }
    }

    @Override
    public void deleteVoteComment(VoteCommentDTO voteCommentDTO) {
        User user = getUser(voteCommentDTO.getUser_id());
        Comment comment = getComment(voteCommentDTO.getComment_id());
        VoteComment voteComment = getVoteComment(user, comment);

        try{
            voteCommentRepository.delete(voteComment);
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

    private Comment getComment(String comment_id){
        Comment comment = null;

        try{
            UUID user_uuid = UUID.fromString(comment_id);
            comment = commentRepository.getById(user_uuid);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Error: Invalid comment");
        }

        if(comment == null){
            throw new ResourceNotFoundException("Error: Comment not found");
        }

        return comment;
    }

    private VoteComment getVoteComment(User user, Comment comment){
        VoteCommentId voteCommentId = new VoteCommentId(user, comment);
        VoteComment voteComment = null;

        try{
            voteComment = voteCommentRepository.findById(voteCommentId).get();
        }
        catch(Exception e){
            throw new ResourceNotFoundException("Error: Could not find vote");
        }

        return voteComment;
    }
}
