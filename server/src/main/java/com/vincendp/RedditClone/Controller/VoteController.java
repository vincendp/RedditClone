package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.VoteCommentDTO;
import com.vincendp.RedditClone.Dto.VotePostDTO;
import com.vincendp.RedditClone.Service.VoteCommentService;
import com.vincendp.RedditClone.Service.VotePostService;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("votes")
public class VoteController {

    private VotePostService votePostService;

    private VoteCommentService voteCommentService;

    @Autowired
    public VoteController(VotePostService votePostService, VoteCommentService voteCommentService){
        this.votePostService = votePostService;
        this.voteCommentService = voteCommentService;
    }

    @PostMapping("/posts")
    public ResponseEntity createVotePost(@RequestBody VotePostDTO votePostDTO){
        validateVotePostDTO(votePostDTO);
        VotePostDTO votePost = votePostService.createVotePost(votePostDTO);
        return ResponseEntity.ok(new SuccessResponse(200, "Success: Created vote", votePost));
    }

    @PutMapping("/posts")
    public ResponseEntity updateVotePost(@RequestBody VotePostDTO votePostDTO){
        validateVotePostDTO(votePostDTO);
        votePostService.updateVotePost(votePostDTO);
        return ResponseEntity.ok(new SuccessResponse(200, "Success: Updated vote", null));
    }

    @DeleteMapping("/posts")
    public ResponseEntity deleteVotePost(@RequestBody VotePostDTO votePostDTO){
        validateVotePostDTO(votePostDTO);
        votePostService.deleteVotePost(votePostDTO);
        return ResponseEntity.ok(new SuccessResponse(200, "Success: Deleted vote", null));
    }

    @PostMapping("/comments")
    public ResponseEntity createVoteComment(@RequestBody VoteCommentDTO voteCommentDTO){
        validateVoteCommentDTO(voteCommentDTO);
        VoteCommentDTO voteComment = voteCommentService.createVoteComment(voteCommentDTO);
        return ResponseEntity.ok(new SuccessResponse(200, "Success: Created vote", voteComment));
    }

    @PutMapping("/comments")
    public ResponseEntity updateVoteComment(@RequestBody VoteCommentDTO voteCommentDTO){
        validateVoteCommentDTO(voteCommentDTO);
        voteCommentService.updateVoteComment(voteCommentDTO);
        return ResponseEntity.ok(new SuccessResponse(200, "Success: Updated vote", null));
    }

    @DeleteMapping("/comments")
    public ResponseEntity deleteVoteComment(@RequestBody VoteCommentDTO voteCommentDTO){
        validateVoteCommentDTO(voteCommentDTO);
        voteCommentService.deleteVoteComment(voteCommentDTO);
        return ResponseEntity.ok(new SuccessResponse(200, "Success: Deleted vote", null));
    }

    private void validateVotePostDTO(VotePostDTO votePostDTO){
        if(votePostDTO.getUser_id() == null || votePostDTO.getUser_id().length() <= 0){
            throw new IllegalArgumentException("Error: User cannot be empty");
        }
        else if(votePostDTO.getPost_id() == null || votePostDTO.getPost_id().length() <= 0){
            throw new IllegalArgumentException("Error: Post cannot be empty");
        }
        else if(votePostDTO.getVote() == null){
            throw new IllegalArgumentException("Error: Vote cannot be empty");
        }
    }

    private void validateVoteCommentDTO(VoteCommentDTO voteCommentDTO){
        if(voteCommentDTO.getUser_id() == null || voteCommentDTO.getUser_id().length() <= 0){
            throw new IllegalArgumentException("Error: User cannot be empty");
        }
        else if(voteCommentDTO.getComment_id() == null || voteCommentDTO.getComment_id().length() <= 0){
            throw new IllegalArgumentException("Error: Comment cannot be empty");
        }
        else if(voteCommentDTO.getVote() == null){
            throw new IllegalArgumentException("Error: Vote cannot be empty");
        }
    }
}
