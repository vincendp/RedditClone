package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.VoteCommentDTO;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql", "/sql/data.sql"})
public class VoteCommentServiceIntegrationTest {

    @Autowired
    private VoteCommentServiceImpl voteCommentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoteCommentRepository voteCommentRepository;

    private User user;

    private Post post;

    private Subreddit subreddit;

    private Comment comment;

    private VoteComment voteComment;

    private VoteCommentDTO voteCommentDTO;

    @BeforeEach
    void init(){
        user = new User(null, "bob", new Date());
        subreddit = new Subreddit(null, "subreddit", new Date());

        userRepository.save(user);
        subredditRepository.save(subreddit);

        post = new Post(null, "title", new Date(), user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString()));

        postRepository.save(post);

        comment = new Comment(null, "Comment", false, null, user, post);

        commentRepository.save(comment);

        voteCommentDTO = new VoteCommentDTO(user.getId().toString(), comment.getId().toString(), true);
        voteComment = new VoteComment(new VoteCommentId(user, comment), true);
    }

    @Test
    void when_create_vote_success_returns_dto(){
        VoteCommentDTO voteCommentDTO = voteCommentService.createVoteComment(this.voteCommentDTO);
        assertNotNull(voteCommentDTO);
        assertEquals(voteCommentDTO.getUser_id(), this.voteCommentDTO.getUser_id());
        assertEquals(voteCommentDTO.getComment_id(), this.voteCommentDTO.getComment_id());
        assertEquals(voteCommentDTO.getVote(), this.voteCommentDTO.getVote());
    }

    @Test
    void when_update_vote_success_should_update_vote(){
        voteCommentService.createVoteComment(voteCommentDTO);
        voteCommentDTO.setVote(false);
        voteCommentService.updateVoteComment(voteCommentDTO);

        VoteComment voteComment = voteCommentRepository.findById(new VoteCommentId(user, comment)).get();
        assertEquals(voteComment.getVote(), voteCommentDTO.getVote());
    }

    @Test
    void when_delete_vote_success_should_delete(){
        VoteCommentDTO voteCommentDTO = voteCommentService.createVoteComment(this.voteCommentDTO);
        assertNotNull(voteCommentDTO);
        voteCommentService.deleteVoteComment(voteCommentDTO);
        Optional<VoteComment> voteComment = voteCommentRepository.findById(new VoteCommentId(user, comment));
        assertFalse(voteComment.isPresent());
    }

}
