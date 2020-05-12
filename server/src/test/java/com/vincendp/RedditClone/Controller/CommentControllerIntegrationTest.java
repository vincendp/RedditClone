package com.vincendp.RedditClone.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vincendp.RedditClone.Dto.CreateCommentRequest;
import com.vincendp.RedditClone.Dto.GetCommentDTO;
import com.vincendp.RedditClone.Dto.GetSubredditResponse;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.CommentRepository;
import com.vincendp.RedditClone.Repository.PostRepository;
import com.vincendp.RedditClone.Repository.SubredditRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import com.vincendp.RedditClone.Service.CommentService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    private CreateCommentRequest createCommentRequest;

    private User user;

    private Post[] posts;

    private Subreddit subreddit;

    @BeforeEach
    void init(){
        user = new User(null, "bob", new Date());
        subreddit = new Subreddit("subreddit");

        userRepository.save(user);
        subredditRepository.save(subreddit);

        posts = new Post[2];
        posts[0] = postRepository.save(new Post(null, "title1", null, user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString())));
        posts[1] = postRepository.save(new Post(null, "title2", null, user, subreddit,
                new PostType(PostType.Type.TEXT.getValue(), PostType.Type.TEXT.toString())));

        commentRepository.save(new Comment(null, "comment1", false, new Date(), user, posts[1]));
        commentRepository.save(new Comment(null, "comment2", false, new Date(), user, posts[1]));
        commentRepository.save(new Comment(null, "comment3", false, new Date(), user, posts[1]));

        createCommentRequest = new CreateCommentRequest(
                "This is a comment", user.getId().toString(), posts[0].getId().toString());
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

    @Test
    void when_post_not_found_returns_status_404() throws Exception{
        mockMvc.perform(get("/posts/{post_id}", UUID.randomUUID().toString())
                .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound());
    }


    @Test
    void when_get_post_with_no_comments_then_return_response_ok_with_empty_list() throws Exception{
        MvcResult result = mockMvc.perform(get("/comments/posts/{post_id}", posts[0].getId().toString())
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, GetCommentDTO.class);
        List<GetCommentDTO> comments = objectMapper.convertValue(successResponse.getResult(), typeReference);
        assertTrue(comments.isEmpty());
    }

    @Test
    void when_get_post_with_comments_then_return_response_ok_with_list() throws Exception{
        MvcResult result = mockMvc.perform(get("/comments/posts/{post_id}", posts[1].getId().toString())
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andReturn();

        SuccessResponse successResponse = objectMapper.readValue(result.getResponse().getContentAsString(), SuccessResponse.class);
        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, GetCommentDTO.class);
        List<GetCommentDTO> comments = objectMapper.convertValue(successResponse.getResult(), typeReference);
        assertEquals(3, comments.size());
    }
}
