package com.vincendp.RedditClone.Service;

import com.vincendp.RedditClone.Model.MyUserDetails;
import com.vincendp.RedditClone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println(s);
        MyUserDetails myUserDetails = userRepository.findUserAndUserAuthentication(s);
        if (myUserDetails == null)
            throw new UsernameNotFoundException("Bad credentials");

        System.out.println(myUserDetails.getUsername());
        System.out.println(myUserDetails.getPassword());
        return myUserDetails;
    }
}
