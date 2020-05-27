package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Dto.CreateUserRequest;
import com.vincendp.RedditClone.Dto.LoginResponse;
import com.vincendp.RedditClone.Exception.ResourceAlreadyExistsException;
import com.vincendp.RedditClone.Exception.ResourceNotFoundException;
import com.vincendp.RedditClone.Model.User;
import com.vincendp.RedditClone.Model.UserAuthentication;
import com.vincendp.RedditClone.Repository.UserAuthenticationRepository;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserAuthenticationRepository userAuthenticationRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserAuthenticationRepository userAuthenticationRepository,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository){
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponse getUserById(String id) {
        User user = null;
        try{
            UUID uuid = UUID.fromString(id);
            user = userRepository.getById(uuid);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Error: Invalid user");
        }
        if(user == null){
            throw new ResourceNotFoundException("Error: User not found");
        }

        return new LoginResponse(user.getId().toString(), user.getUsername(), user.getCreated_at());
    }

    @Override
    public LoginResponse getUserByName(String username) {
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new ResourceNotFoundException("Error: User not found");
        }

        return new LoginResponse(user.getId().toString(), user.getUsername(), user.getCreated_at());
    }

    @Override
    public LoginResponse createUser(CreateUserRequest createUserRequest) {
        User existingUser = userRepository.findByUsername(createUserRequest.getUsername());

        if(existingUser != null){
            throw new ResourceAlreadyExistsException("Error: User already exists");
        }

        User u = new User();
        UserAuthentication userAuthentication = new UserAuthentication();

        u.setUsername(createUserRequest.getUsername());
        userAuthentication.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        userAuthentication.setUser(u);

        try{
            userAuthentication = userAuthenticationRepository.save(userAuthentication);
        }
        catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Error: Could not save user");
        }

        u.setId(userAuthentication.getUser_id());
        u.setCreated_at(userAuthentication.getUser().getCreated_at());

        return new LoginResponse(u.getId().toString(), u.getUsername(), u.getCreated_at());
    }
}
