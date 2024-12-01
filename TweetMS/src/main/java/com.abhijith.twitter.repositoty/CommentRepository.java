package com.abhijith.twitter.repositoty;

import com.abhijith.twitter.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByTweetId(String tweetId);
    void deleteByTweetId(String tweetId);
    Comment findByTweetIdAndUserId(String tweetId, String userId);
}

