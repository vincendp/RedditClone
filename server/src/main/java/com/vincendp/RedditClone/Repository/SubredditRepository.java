package com.vincendp.RedditClone.Repository;

import com.vincendp.RedditClone.Model.Subreddit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubredditRepository extends CrudRepository<Subreddit, UUID> {

    Subreddit getById(UUID id);
    Subreddit findByName(String name);

}
