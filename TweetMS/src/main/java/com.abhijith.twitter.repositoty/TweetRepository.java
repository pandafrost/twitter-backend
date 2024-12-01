package com.abhijith.twitter.repositoty;

import com.abhijith.twitter.entity.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TweetRepository extends MongoRepository<Tweet, String> {
    List<Tweet> findByUserId(String userId);
}

