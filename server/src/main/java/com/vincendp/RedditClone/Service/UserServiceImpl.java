package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserAuthenticationRepository userAuthenticationRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserAuthenticationRepository userAuthenticationRepository,
                           PasswordEncoder passwordEncoder){
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse createUser(CreateUserRequest createUserRequest) {
        User u = new User();
        UserAuthentication userAuthentication = new UserAuthentication();

        u.setUsername(createUserRequest.getUsername());
        userAuthentication.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        userAuthentication.setUser(u);

        userAuthenticationRepository.save(userAuthentication);

        return new LoginResponse(u.getId().toString(), u.getUsername(), u.getCreated_at());
    }
}
