package com.vincendp.RedditClone.Repository;

import com.vincendp.RedditClone.Model.UserAuthentication;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserAuthenticationRepository extends CrudRepository<UserAuthentication, UUID> {

}
