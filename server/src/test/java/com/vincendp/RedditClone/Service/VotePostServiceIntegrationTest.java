package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.VotePostDTO;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Repository.VotePostRepository;
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
public class VotePostServiceIntegrationTest {

    @Autowired
    private VotePostService votePostService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private VotePostRepository votePostRepository;

    private User user;

    private Post post;

    private Subreddit subreddit;

    private VotePostDTO votePostDTO;

    @BeforeEach
    void init(){
        user = new User(null, "bob", new Date());
        subreddit = new Subreddit(null, "subreddit", new Date());

        userRepository.save(user);
        subredditRepository.save(subreddit);

        post = new Post(null, "title", new Date(), user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString()));

        postRepository.save(post);

        votePostDTO = new VotePostDTO(user.getId().toString(), post.getId().toString(), true);
    }

    @Test
    void when_create_vote_success_returns_dto(){
        VotePostDTO votePostDTO = votePostService.createVotePost(this.votePostDTO);
        assertNotNull(votePostDTO);
        assertEquals(votePostDTO.getUser_id(), this.votePostDTO.getUser_id());
        assertEquals(votePostDTO.getPost_id(), this.votePostDTO.getPost_id());
        assertEquals(votePostDTO.getVote(), this.votePostDTO.getVote());
    }

    @Test
    void when_update_vote_success_should_update_vote(){
        votePostService.createVotePost(votePostDTO);
        votePostDTO.setVote(false);
        votePostService.updateVotePost(votePostDTO);

        VotePost votePost = votePostRepository.findById(new VotePostId(user, post)).get();
        assertEquals(votePost.getVote(), votePostDTO.getVote());
    }

    @Test
    void when_delete_vote_success_should_delete(){
        VotePostDTO votePostDTO = votePostService.createVotePost(this.votePostDTO);
        assertNotNull(votePostDTO);
        votePostService.deleteVotePost(votePostDTO);
        Optional<VotePost> votePost = votePostRepository.findById(new VotePostId(user, post));
        assertFalse(votePost.isPresent());
    }
}
