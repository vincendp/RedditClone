package com.vincendp.RedditClone.Repository;


import com.vincendp.RedditClone.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

}
