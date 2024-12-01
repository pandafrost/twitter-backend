package com.abhijith.twitter.repositoty;

import com.abhijith.twitter.entity.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LikeRepository extends MongoRepository<Like, String> {
    List<Like> findByTweetId(String tweetId);
    void deleteByTweetId(String tweetId);
    Like findByTweetIdAndUserId(String tweetId, String userId);
}

