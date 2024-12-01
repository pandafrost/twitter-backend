package com.abhijith.twitter.repository;

import com.abhijith.twitter.entity.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends MongoRepository<Follow, String> {
    List<Follow> findByFollowerId(String followerId);
    List<Follow> findByFollowingId(String followingId);
    Optional<Follow> findByFollowerIdAndFollowingId(String followerId, String followingId);
}

