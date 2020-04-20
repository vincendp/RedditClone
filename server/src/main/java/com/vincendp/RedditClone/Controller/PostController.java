package com.vincendp.RedditClone.Controller;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Model.PostType;
import com.vincendp.RedditClone.Service.PostService;
import com.vincendp.RedditClone.Utility.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("posts")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping(consumes = { "multipart/form-data" })
    ResponseEntity createPost(@ModelAttribute("createPostForm") CreatePostRequest createPostRequest){
        System.out.println("tite: " + createPostRequest.getTitle());
        System.out.println("description:" + createPostRequest.getDescription());
        System.out.println("post_type:" + createPostRequest.getPost_type());
        System.out.println("user_id:" + createPostRequest.getUser_id());
        System.out.println("subreddit_id" + createPostRequest.getSubreddit_id());
        System.out.println("link: " + createPostRequest.getLink());
        System.out.println("image:");
        System.out.println(createPostRequest.getImage());
        System.out.println(createPostRequest.getImage().getContentType());

        if( createPostRequest.getTitle() == null
                || createPostRequest.getTitle().length() <= 0){
            throw new IllegalArgumentException("Error: Title cannot be empty.");
        }
        else if (createPostRequest.getUser_id() == null
                || createPostRequest.getSubreddit_id() == null
                || createPostRequest.getUser_id().length() <= 0
                || createPostRequest.getSubreddit_id().length() <= 0){
            throw new IllegalArgumentException("Error: User or subreddit cannot be empty.");
        }
        else if(createPostRequest.getPost_type() == null){
            throw new IllegalArgumentException("Error: Post must have a type");
        }
        else if(createPostRequest.getPost_type() == PostType.Type.IMAGE.getValue() && (
                createPostRequest.getImage().isEmpty() || (!createPostRequest.getImage().getContentType().endsWith("jpg")
                        && !createPostRequest.getImage().getContentType().endsWith("jpeg")
                        && !createPostRequest.getImage().getContentType().endsWith("png")))){
            throw new IllegalArgumentException("Error: Image post cannot have empty image");
        }
        else if(createPostRequest.getPost_type() == PostType.Type.LINK.getValue()
                && (createPostRequest.getLink() == null || createPostRequest.getLink().length() <= 0)){
            throw new IllegalArgumentException("Error: Link post cannot have empty link");
        }
        else if(createPostRequest.getPost_type() != PostType.Type.TEXT.getValue()
                && createPostRequest.getPost_type() != PostType.Type.IMAGE.getValue()
                && createPostRequest.getPost_type() != PostType.Type.LINK.getValue()){
            throw new IllegalArgumentException("Error: Invalid post type");
        }

        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);

        return ResponseEntity.ok(new SuccessResponse(200, "Success: Created post", createPostResponse));
    }
}
