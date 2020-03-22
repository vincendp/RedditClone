package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Model.CustomUserDetails;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void when_username_not_found_throws_error(){
        lenient().when(userRepository.findUserAndUserAuthentication(anyString())).thenReturn((CustomUserDetails) null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("bob");
        });
    }

    @Test
    void when_username_found_returns_user_details(){
        CustomUserDetails customUserDetails = new CustomUserDetails(new User(), new UserAuthentication());
        lenient().when(userRepository.findUserAndUserAuthentication(anyString())).thenReturn(customUserDetails);

        assertNotNull(userDetailsService.loadUserByUsername("bob"));
        assertEquals(customUserDetails, userDetailsService.loadUserByUsername("bob"));
    }

}
