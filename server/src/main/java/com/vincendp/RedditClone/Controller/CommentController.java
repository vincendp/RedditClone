package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.CreateCommentRequest;
import com.vincendp.RedditClone.Dto.CreateCommentResponse;
import com.vincendp.RedditClone.Dto.GetCommentDTO;
import com.vincendp.RedditClone.Service.CommentService;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comments")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/posts/{post_id}")
    public ResponseEntity getCommentsFromPost(@PathVariable String post_id){
        List<GetCommentDTO> dtos = commentService.getCommentsFromPost(post_id);
        return ResponseEntity.ok(new SuccessResponse(200, "Success: Got comments", dtos));
    }

    @PostMapping
    public ResponseEntity createComment(@RequestBody CreateCommentRequest createCommentRequest){
        if(createCommentRequest.getComment() == null || createCommentRequest.getComment().length() <= 0){
            throw new IllegalArgumentException("Error: Comment cannot be empty");
        }
        else if(createCommentRequest.getPost_id() == null){
            throw new IllegalArgumentException("Error: Comment cannot be empty");
        }
        else if(createCommentRequest.getUser_id() == null || createCommentRequest.getUser_id().length() <= 0
            || createCommentRequest.getPost_id() == null || createCommentRequest.getPost_id().length() <= 0){
            throw new IllegalArgumentException("Error: Post or User cannot be empty");
        }

        CreateCommentResponse createCommentResponse = commentService.createComment(createCommentRequest);
        return ResponseEntity.ok(new SuccessResponse(200, "Success: Created comment", createCommentResponse));
    }

}
