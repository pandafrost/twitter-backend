package com.abhijith.twitter.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;
    private String firstname;
    private String email;
    private String password;
    private String profilePic;
    private String coverPic;
    private String bio;
    private String location;
    private String website;
    private boolean isPrivate;
}

