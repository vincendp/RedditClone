package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreatePostRequest;
import com.vincendp.RedditClone.Dto.CreatePostResponse;
import com.vincendp.RedditClone.Dto.GetPostDTO;
import com.vincendp.RedditClone.Dto.GetPostPreviewDTO;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.*;
import com.vincendp.RedditClone.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Sql({"/sql/redditdb.sql", "/sql/data.sql"})
public class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private PostTypeRepository postTypeRepository;

    @Autowired
    private VotePostRepository votePostRepository;

    private CreatePostRequest createPostRequest;

    private Subreddit[] subreddits;

    private Post[] posts;

    private User[] users;

    private UserAuthentication[] userAuthentications;

    @BeforeEach
    void setup(){
        users = new User[6];
        subreddits = new Subreddit[2];
        subreddits[0] = subredditRepository.save(new Subreddit(null, "subreddit1", new Date()));
        subreddits[1] = subredditRepository.save(new Subreddit(null, "subreddit2", new Date()));

        users[0] = userRepository.save(new User(null, "bob", new Date()));
        users[1] = userRepository.save(new User(null, "bob2", new Date()));
        users[2] = userRepository.save(new User(null, "bob3", new Date()));
        users[3] = userRepository.save(new User(null, "bob4", new Date()));
        users[4] = userRepository.save(new User(null, "bob5", new Date()));
        users[5] = userRepository.save(new User(null, "bob6", new Date()));

        userAuthentications = new UserAuthentication[3];
        userAuthentications[0] = userAuthenticationRepository.save(new UserAuthentication(users[0].getId(), "123456", users[0]));
        userAuthentications[0] = userAuthenticationRepository.save(new UserAuthentication(users[1].getId(), "123456", users[1]));
        userAuthentications[0] = userAuthenticationRepository.save(new UserAuthentication(users[2].getId(), "123456", users[2]));

        PostType postType = postTypeRepository.findById(PostType.Type.TEXT.getValue()).get();
        createPostRequest = new CreatePostRequest("title1", "description",
                "https://www.google.com",
                new MockMultipartFile("image", "image1.jpeg", "image/jpeg", "image1".getBytes()),
                users[0].getId().toString(), subreddits[0].getId().toString(), PostType.Type.TEXT.getValue());

        posts = new Post[6];

        posts[0] = postRepository.save(new Post(null, "title1", new Date(), users[0], subreddits[0], postType));
        posts[1] = postRepository.save(new Post(null, "title2", new Date(), users[0], subreddits[0], postType));
        posts[2] = postRepository.save(new Post(null, "title3", new Date(), users[0], subreddits[1], postType));
        posts[3] = postRepository.save(new Post(null, "title4", new Date(), users[1], subreddits[1], postType));
        posts[4] = postRepository.save(new Post(null, "title5", new Date(), users[2], subreddits[1], postType));
        posts[5] = postRepository.save(new Post(null, "title6", new Date(), users[2], subreddits[1], postType));

        votePostRepository.save(new VotePost(new VotePostId(users[0], posts[0]), true));
        votePostRepository.save(new VotePost(new VotePostId(users[1], posts[0]), true));
        votePostRepository.save(new VotePost(new VotePostId(users[2], posts[0]), false));
        votePostRepository.save(new VotePost(new VotePostId(users[3], posts[0]), true));
        votePostRepository.save(new VotePost(new VotePostId(users[4], posts[0]), true));
    }

    @Test
    void when_subreddit_not_found_should_throw_error(){
        createPostRequest.setSubreddit_id(UUID.randomUUID().toString());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_user_not_found_should_throw_error(){
        createPostRequest.setUser_id(UUID.randomUUID().toString());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_post_type_not_found_should_throw_error(){
        createPostRequest.setPost_type(-1);

        assertThrows(NoSuchElementException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_image_post_with_invalid_image_should_throw_error(){
        createPostRequest.setPost_type(PostType.Type.IMAGE.getValue());
        createPostRequest.setImage(null);

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });

        createPostRequest.setImage(
                new MockMultipartFile("image", "image1.jpeg",
                        "image/jpeg", new byte[0]));

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_link_post_with_no_link_should_throw_error(){
        createPostRequest.setPost_type(PostType.Type.LINK.getValue());
        createPostRequest.setLink(null);

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_link_post_with_invalid_link_should_throw_error(){
        createPostRequest.setPost_type(PostType.Type.LINK.getValue());
        createPostRequest.setLink("www.");

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(createPostRequest);
        });
    }

    @Test
    void when_valid_text_post_should_return_response(){
        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);

        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddits[0].getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(users[0].getId().toString(), createPostResponse.getUser_id());
        assertEquals(posts[0].getTitle(), createPostResponse.getTitle());
    }

    @Test
    void when_valid_image_post_should_return_response(){
        createPostRequest.setPost_type(PostType.Type.IMAGE.getValue());

        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddits[0].getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(users[0].getId().toString(), createPostResponse.getUser_id());
        assertEquals(posts[0].getTitle(), createPostResponse.getTitle());

        createPostRequest.setImage(
                new MockMultipartFile("image", "image1.jpg",
                        "image/jpg", "image".getBytes()));

        createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddits[0].getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(users[0].getId().toString(), createPostResponse.getUser_id());
        assertEquals(posts[0].getTitle(), createPostResponse.getTitle());

        createPostRequest.setImage(
                new MockMultipartFile("image", "image1.png",
                        "image/png", "image".getBytes()));

        createPostResponse = postService.createPost(createPostRequest);
        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddits[0].getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(users[0].getId().toString(), createPostResponse.getUser_id());
        assertEquals(posts[0].getTitle(), createPostResponse.getTitle());
    }

    @Test
    void when_valid_link_post_should_return_response(){
        createPostRequest.setPost_type(PostType.Type.LINK.getValue());
        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);

        assertNotNull(createPostResponse);
        assertNotNull(createPostResponse.getId());
        assertEquals(subreddits[0].getId().toString(), createPostResponse.getSubreddit_id());
        assertEquals(users[0].getId().toString(), createPostResponse.getUser_id());
        assertEquals(posts[0].getTitle(), createPostResponse.getTitle());
    }

    @Test
    void when_post_not_found_throws_error(){
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[0], userAuthentications[0])
                        , null));
        assertThrows(ResourceNotFoundException.class, () -> {
            postService.getPost(UUID.randomUUID().toString());
        });
    }

    @Test
    void when_auth_user_with_post_created_by_user_returns_dto(){
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[0], userAuthentications[0])
                , null));
        GetPostDTO getPostDTO = postService.getPost(posts[0].getId().toString());
        assertNotNull(getPostDTO);
        assertEquals(getPostDTO.getTitle(), posts[0].getTitle());
        assertTrue(getPostDTO.getUser_voted_for_post() > 0);
        assertEquals(3, getPostDTO.getVotes());
    }

    @Test
    void when_auth_user_with_post_not_created_by_user_returns_dto(){
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[5], userAuthentications[0])
                        , null));
        GetPostDTO getPostDTO = postService.getPost(posts[0].getId().toString());
        assertNotNull(getPostDTO);
        assertEquals(getPostDTO.getTitle(), posts[0].getTitle());
        assertTrue(getPostDTO.getUser_voted_for_post() == 0);
        assertEquals(3, getPostDTO.getVotes());
    }

    @Test
    void when_no_auth_user_returns_dto(){
        GetPostDTO getPostDTO = postService.getPost(posts[0].getId().toString());
        assertNotNull(getPostDTO);
        assertEquals(getPostDTO.getTitle(), posts[0].getTitle());
        assertTrue(getPostDTO.getUser_voted_for_post() == 0);
        assertEquals(3, getPostDTO.getVotes());
    }

    @Test
    void when_auth_user_with_all_post_previews_should_return_dto(){
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[0], userAuthentications[0])
                        , null));
        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviews();
        postPreviews.sort(Comparator.comparing(GetPostPreviewDTO::getTitle));

        assertNotNull(postPreviews);
        assertEquals(6, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() > 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
        assertEquals(users[0].getId().toString(), postPreviews.get(0).getUser_id());
        assertEquals(users[0].getId().toString(), postPreviews.get(1).getUser_id());
        assertEquals(users[0].getId().toString(), postPreviews.get(2).getUser_id());
        assertNotEquals(users[0].getId().toString(), postPreviews.get(3).getUser_id());
        assertNotEquals(users[0].getId().toString(), postPreviews.get(4).getUser_id());
        assertNotEquals(users[0].getId().toString(), postPreviews.get(5).getUser_id());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[1], userAuthentications[1])
                        , null));

        postPreviews = postService.getAllPostPreviews();
        postPreviews.sort(Comparator.comparing(GetPostPreviewDTO::getTitle));

        assertNotNull(postPreviews);
        assertEquals(6, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() > 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
        assertNotEquals(users[1].getId().toString(), postPreviews.get(0).getUser_id());
        assertNotEquals(users[1].getId().toString(), postPreviews.get(1).getUser_id());
        assertNotEquals(users[1].getId().toString(), postPreviews.get(2).getUser_id());
        assertEquals(users[1].getId().toString(), postPreviews.get(3).getUser_id());
        assertNotEquals(users[1].getId().toString(), postPreviews.get(4).getUser_id());
        assertNotEquals(users[1].getId().toString(), postPreviews.get(5).getUser_id());


        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[2], userAuthentications[2])
                        , null));

        postPreviews = postService.getAllPostPreviews();
        postPreviews.sort(Comparator.comparing(GetPostPreviewDTO::getTitle));

        assertNotNull(postPreviews);
        assertEquals(6, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() < 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
        assertNotEquals(users[2].getId().toString(), postPreviews.get(0).getUser_id());
        assertNotEquals(users[2].getId().toString(), postPreviews.get(1).getUser_id());
        assertNotEquals(users[2].getId().toString(), postPreviews.get(2).getUser_id());
        assertNotEquals(users[2].getId().toString(), postPreviews.get(3).getUser_id());
        assertEquals(users[2].getId().toString(), postPreviews.get(4).getUser_id());
        assertEquals(users[2].getId().toString(), postPreviews.get(5).getUser_id());
    }

    @Test
    void when_without_auth_with_all_post_previews_should_return_dto(){
        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviews();
        postPreviews.sort(Comparator.comparing(GetPostPreviewDTO::getTitle));
        assertNotNull(postPreviews);
        assertEquals(6, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() == 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
    }

    @Test
    void when_get_all_post_previews_by_user_and_user_has_no_posts_should_return_empty_list(){
        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviewsByUser(UUID.randomUUID().toString());
        assertNotNull(postPreviews);
        assertEquals(0, postPreviews.size());
    }

    @Test
    void when_auth_user_with_all_post_previews_by_specific_user_should_return_dto(){
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[0], userAuthentications[0])
                        , null));
        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviewsByUser(users[0].getId().toString());
        postPreviews.sort(Comparator.comparing(GetPostPreviewDTO::getTitle));
        assertNotNull(postPreviews);
        assertEquals(3, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() > 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
        assertEquals(users[0].getId().toString(), postPreviews.get(0).getUser_id());
        assertEquals(users[0].getId().toString(), postPreviews.get(1).getUser_id());
        assertEquals(users[0].getId().toString(), postPreviews.get(2).getUser_id());


        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[1], userAuthentications[1])
                        , null));
        postPreviews = postService.getAllPostPreviewsByUser(users[0].getId().toString());
        postPreviews.sort(Comparator.comparing(GetPostPreviewDTO::getTitle));
        assertNotNull(postPreviews);
        assertEquals(3, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() > 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
        assertNotEquals(users[1].getId().toString(), postPreviews.get(0).getUser_id());
        assertNotEquals(users[1].getId().toString(), postPreviews.get(1).getUser_id());
        assertNotEquals(users[1].getId().toString(), postPreviews.get(2).getUser_id());


        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[1], userAuthentications[1])
                        , null));
        postPreviews = postService.getAllPostPreviewsByUser(users[1].getId().toString());
        assertNotNull(postPreviews);
        assertEquals(1, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() == 0);
        assertEquals(users[1].getId().toString(), postPreviews.get(0).getUser_id());


        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[2], userAuthentications[2])
                        , null));
        postPreviews = postService.getAllPostPreviewsByUser(users[2].getId().toString());
        assertNotNull(postPreviews);
        assertEquals(2, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() == 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
        assertEquals(users[2].getId().toString(), postPreviews.get(0).getUser_id());
        assertEquals(users[2].getId().toString(), postPreviews.get(1).getUser_id());
    }

    @Test
    void when_without_auth_with_all_post_previews_by_specific_user_should_return_dto(){
        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviewsByUser(users[0].getId().toString());
        assertEquals(3, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() == 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);

        postPreviews = postService.getAllPostPreviewsByUser(users[1].getId().toString());
        assertEquals(1, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() == 0);

        postPreviews = postService.getAllPostPreviewsByUser(users[2].getId().toString());
        assertEquals(2, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() == 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
    }

    @Test
    void when_get_all_post_previews_by_subreddit_and_subreddit_has_no_posts_should_return_empty_list(){
        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviewsBySubreddit(UUID.randomUUID().toString());
        assertNotNull(postPreviews);
        assertEquals(0, postPreviews.size());
    }

    @Test
    void when_auth_user_with_all_post_previews_by_specific_subreddit_should_return_dto(){
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[0], userAuthentications[0])
                        , null));
        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviewsBySubreddit(subreddits[0].getId().toString());
        postPreviews.sort(Comparator.comparing(GetPostPreviewDTO::getTitle));
        assertNotNull(postPreviews);
        assertEquals(2, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() > 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
        assertEquals(users[0].getId().toString(), postPreviews.get(0).getUser_id());
        assertEquals(users[0].getId().toString(), postPreviews.get(1).getUser_id());
        assertEquals(subreddits[0].getId().toString(), postPreviews.get(0).getSubreddit_id());
        assertEquals(subreddits[0].getId().toString(), postPreviews.get(1).getSubreddit_id());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(users[1], userAuthentications[0])
                        , null));
        postPreviews = postService.getAllPostPreviewsBySubreddit(subreddits[1].getId().toString());
        postPreviews.sort(Comparator.comparing(GetPostPreviewDTO::getTitle));
        assertNotNull(postPreviews);
        assertEquals(4, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() == 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
        assertEquals(users[0].getId().toString(), postPreviews.get(0).getUser_id());
        assertEquals(users[1].getId().toString(), postPreviews.get(1).getUser_id());
        assertEquals(users[2].getId().toString(), postPreviews.get(2).getUser_id());
        assertEquals(subreddits[1].getId().toString(), postPreviews.get(0).getSubreddit_id());
        assertEquals(subreddits[1].getId().toString(), postPreviews.get(1).getSubreddit_id());
        assertEquals(subreddits[1].getId().toString(), postPreviews.get(2).getSubreddit_id());
        assertEquals(subreddits[1].getId().toString(), postPreviews.get(3).getSubreddit_id());
    }

    @Test
    void when_without_auth_with_all_post_previews_by_specific_subreddit_should_return_dto(){
        List<GetPostPreviewDTO> postPreviews = postService.getAllPostPreviewsBySubreddit(subreddits[0].getId().toString());
        assertEquals(2, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() == 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
        assertEquals(subreddits[0].getId().toString(), postPreviews.get(0).getSubreddit_id());
        assertEquals(subreddits[0].getId().toString(), postPreviews.get(1).getSubreddit_id());

        postPreviews = postService.getAllPostPreviewsBySubreddit(subreddits[1].getId().toString());
        assertEquals(4, postPreviews.size());
        assertTrue(postPreviews.get(0).getUser_voted_for_post() == 0);
        assertTrue(postPreviews.get(1).getUser_voted_for_post() == 0);
        assertEquals(subreddits[1].getId().toString(), postPreviews.get(0).getSubreddit_id());
        assertEquals(subreddits[1].getId().toString(), postPreviews.get(1).getSubreddit_id());
        assertEquals(subreddits[1].getId().toString(), postPreviews.get(2).getSubreddit_id());
        assertEquals(subreddits[1].getId().toString(), postPreviews.get(3).getSubreddit_id());
    }
}
