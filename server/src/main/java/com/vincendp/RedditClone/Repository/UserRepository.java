package com.vincendp.RedditClone.Repository;


import com.vincendp.RedditClone.Model.CustomUserDetails;
import com.vincendp.RedditClone.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    @Query("SELECT new com.vincendp.RedditClone.Model.CustomUserDetails(u, ua)" +
            "FROM User u, UserAuthentication ua WHERE u.id=ua.user_id and u.username = :username")
    CustomUserDetails findUserAndUserAuthentication(String username);
}
