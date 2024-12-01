package com.abhijith.twitter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tweets")
public class Tweet {

    @Id
    private String id;
    private String userId;
    private String tweetText;
    private String linkURL;
    private Integer likes;
    private List<String> mentions;
    private List<String> hashtags;
    private LocalDateTime createdTimeStamp;
    private LocalDateTime updatedTimeStamp;
}

