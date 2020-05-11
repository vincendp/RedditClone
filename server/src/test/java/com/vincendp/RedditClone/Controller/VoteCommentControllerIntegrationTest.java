package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincendp.RedditClone.Dto.VoteCommentDTO;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.CommentRepository;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Utility.SuccessResponse;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql", "/sql/data.sql"})
public class VoteCommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private CommentRepository commentRepository;

    private VoteCommentDTO voteCommentDTO;

    @BeforeEach
    void init(){
        User user = new User(null, "bob", new Date());
        Subreddit subreddit = new Subreddit(null, "subreddit", new Date());

        userRepository.save(user);
        subredditRepository.save(subreddit);

        Post post = new Post(null, "title", new Date(), user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString()));

        postRepository.save(post);

        Comment comment = new Comment(null, "Comment", false, null, user, post);

        commentRepository.save(comment);

        voteCommentDTO = new VoteCommentDTO(user.getId().toString(), comment.getId().toString(), true);
    }

    @Test
    @WithMockUser("mockUser")
    void when_create_comment_success_returns_response_ok() throws Exception{
        String json = objectMapper.writeValueAsString(voteCommentDTO);

        MvcResult result = mockMvc.perform(post("/votes/comments")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        VoteCommentDTO voteCommentDTO = objectMapper.convertValue(successResponse.getResult(), VoteCommentDTO.class);
        assertNotNull(voteCommentDTO);
        assertEquals(voteCommentDTO.getComment_id(), this.voteCommentDTO.getComment_id());
        assertEquals(voteCommentDTO.getUser_id(), this.voteCommentDTO.getUser_id());
    }

    @Test
    @WithMockUser("mockUser")
    void when_update_comment_success_returns_response_ok() throws Exception{
        String json = objectMapper.writeValueAsString(voteCommentDTO);
        mockMvc.perform(post("/votes/comments")
                .header("Content-Type", "application/json")
                .content(json));

        voteCommentDTO.setVote(false);
        json = objectMapper.writeValueAsString(voteCommentDTO);

        mockMvc.perform(put("/votes/comments")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Updated vote")));
    }

    @Test
    @WithMockUser("mockUser")
    void when_delete_comment_success_returns_response_ok() throws Exception{
        String json = objectMapper.writeValueAsString(voteCommentDTO);
        mockMvc.perform(post("/votes/comments")
                .header("Content-Type", "application/json")
                .content(json));

        mockMvc.perform(delete("/votes/comments")
                .header("Content-Type", "application/json")
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success: Deleted vote")));
    }

}
