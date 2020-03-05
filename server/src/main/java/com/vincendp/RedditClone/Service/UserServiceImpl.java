package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.CreateUserResponse;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserAuthenticationRepository userAuthenticationRepository;
    private UserAuthenticationService userAuthenticationService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserAuthenticationRepository userAuthenticationRepository,
                           UserAuthenticationService userAuthenticationService,
                           PasswordEncoder passwordEncoder){
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userAuthenticationService = userAuthenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        User u = new User();
        UserAuthentication userAuthentication = new UserAuthentication();

        u.setUsername(createUserRequest.getUsername());
        userAuthentication.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

        String a = passwordEncoder.encode(createUserRequest.getPassword());

        System.out.println("hi");
        System.out.println(a.length());


        userAuthentication.setUser(u);

        userAuthentication = userAuthenticationRepository.save(userAuthentication);

        System.out.println(userAuthentication.getUser_id());
        System.out.println(userAuthentication.getPassword());

        u = userAuthentication.getUser();
        System.out.println(u.getId());
        System.out.println(u.getUsername());


        return new CreateUserResponse();
    }
}
