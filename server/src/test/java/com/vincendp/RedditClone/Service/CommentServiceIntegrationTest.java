package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateCommentRequest;
import com.vincendp.RedditClone.Dto.CreateCommentResponse;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql", "/sql/data.sql"})
public class CommentServiceIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private VoteCommentRepository voteCommentRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    private Subreddit subreddit;

    private Post[] posts;

    private User[] users;

    private Comment[] comments;

    private CreateCommentRequest createCommentRequest;

    private UserAuthentication userAuthentication;

    @BeforeEach
    void init(){
        users = new User[6];
        users[0] = userRepository.save(new User(null, "bob1", null));
        users[1] = userRepository.save(new User(null, "bob2", null));
        users[2] = userRepository.save(new User(null, "bob3", null));
        users[3] = userRepository.save(new User(null, "bob4", null));
        users[4] = userRepository.save(new User(null, "bob5", null));
        users[5] = userRepository.save(new User(null, "bob6", null));

        subreddit = subredditRepository.save(new Subreddit(null, "subreddit", null));

        userAuthentication = userAuthenticationRepository.save(new UserAuthentication(users[0].getId(), "123456", users[0]));

        posts = new Post[2];
        posts[0] = postRepository.save(new Post(null, "title1", null, users[0], subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString())));
        posts[1] = postRepository.save(new Post(null, "title2", null, users[0], subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString())));

        comments = new Comment[2];
        comments[0] = commentRepository.save(new Comment(null, "comment1", false, new Date(), users[0], posts[0]));
        comments[1] = commentRepository.save(new Comment(null, "comment2", false, new Date(), users[1], posts[0]));

        voteCommentRepository.save(new VoteComment(new VoteCommentId(users[0], comments[0]), true));
        voteCommentRepository.save(new VoteComment(new VoteCommentId(users[1], comments[0]), false));
        voteCommentRepository.save(new VoteComment(new VoteCommentId(users[2], comments[0]), true));
        voteCommentRepository.save(new VoteComment(new VoteCommentId(users[3], comments[0]), true));
        voteCommentRepository.save(new VoteComment(new VoteCommentId(users[4], comments[0]), true));

        voteCommentRepository.save(new VoteComment(new VoteCommentId(users[1], comments[1]), false));
        voteCommentRepository.save(new VoteComment(new VoteCommentId(users[2], comments[1]), false));
        voteCommentRepository.save(new VoteComment(new VoteCommentId(users[3], comments[1]), false));
        voteCommentRepository.save(new VoteComment(new VoteCommentId(users[4], comments[1]), true));
        voteCommentRepository.save(new VoteComment(new VoteCommentId(users[5], comments[1]), false));

        createCommentRequest = new CreateCommentRequest("Comment"
                , users[0].getId().toString(), posts[0].getId().toString());
    }

    @Test
    void when_user_not_found_throw_error(){
        createCommentRequest.setUser_id(UUID.randomUUID().toString());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(createCommentRequest);
        });
    }

    @Test
    void when_post_not_found_throw_error(){
        createCommentRequest.setPost_id(UUID.randomUUID().toString());

        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(createCommentRequest);
        });
    }

    @Test
    void when_valid_comment_should_return_response(){
        CreateCommentResponse createCommentResponse = commentService.createComment(createCommentRequest);

        assertNotNull(createCommentResponse);
        assertNotNull(createCommentResponse.getId());
        assertEquals(createCommentResponse.getComment(), createCommentRequest.getComment());
    }

    @Test
    void when_post_not_found_throws_error(){
        assertThrows(ResourceNotFoundException.class, () -> {
            commentService.getCommentsFromPost(UUID.randomUUID().toString());
        });
    }


}
