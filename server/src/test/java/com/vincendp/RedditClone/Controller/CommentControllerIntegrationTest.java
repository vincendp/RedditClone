package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Dto.CreateCommentRequest;
import com.vincendp.RedditClone.Model.Post;
import com.vincendp.RedditClone.Model.PostType;
import com.vincendp.RedditClone.Model.Subreddit;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Service.CommentService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql", "/sql/data.sql"})
public class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    private CreateCommentRequest createCommentRequest;

    private User user;

    private Post post;

    private Subreddit subreddit;

    @BeforeEach
    void init(){
        user = new User(null, "bob", new Date());
        subreddit = new Subreddit("subreddit");

        userRepository.save(user);
        subredditRepository.save(subreddit);

        post = new Post(null, "title", null, user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString()));

        postRepository.save(post);
        createCommentRequest = new CreateCommentRequest(
                "This is a comment", user.getId().toString(), post.getId().toString());
    }

    @Test
    @WithMockUser("mockUser")
    void when_valid_comment_returns_response_ok() throws Exception{
        String json = objectMapper.writeValueAsString(createCommentRequest);

        mockMvc.perform(post("/comments")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Created comment")));
    }

}
