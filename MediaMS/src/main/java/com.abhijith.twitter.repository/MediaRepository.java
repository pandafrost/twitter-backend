package com.abhijith.twitter.repository;

import com.abhijith.twitter.entity.Media;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends MongoRepository<Media, String> {
    List<Media> findByTweetId(String tweetId);
    void deleteByTweetId(String tweetId);
}
