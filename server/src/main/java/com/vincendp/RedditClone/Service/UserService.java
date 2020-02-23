package com.vincendp.RedditClone.Service;


import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.CreateUserResponse;

public interface UserService {

    CreateUserResponse createUser(CreateUserRequest createUserRequest);

}
