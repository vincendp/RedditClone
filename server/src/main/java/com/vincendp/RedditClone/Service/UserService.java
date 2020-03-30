package com.vincendp.RedditClone.Service;


import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;

public interface UserService {

    LoginResponse getUser(String id);
    LoginResponse createUser(CreateUserRequest createUserRequest);

}
