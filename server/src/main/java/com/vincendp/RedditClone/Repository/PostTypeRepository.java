package com.vincendp.RedditClone.Repository;

import com.vincendp.RedditClone.Model.PostType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTypeRepository extends CrudRepository<PostType, Integer> {
}
