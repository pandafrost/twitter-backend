package com.abhijith.twitter.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Media {

    @Id
    private String id;
    private String tweetId;
    private String mediaUrl;
}

