package com.abhijith.twitter.repository;

import com.abhijith.twitter.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findById(String id);
    User findByEmail(String email);
    List<User> findByFirstnameStartingWith(String name);

}
