package com.abhijith.twitter.service;

import com.abhijith.twitter.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class MongoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public User findById(String id) {
        return mongoTemplate.findById(new ObjectId(id), User.class);
    }
}

