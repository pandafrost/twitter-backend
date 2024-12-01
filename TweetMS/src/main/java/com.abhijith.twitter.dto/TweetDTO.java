package com.abhijith.twitter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetDTO {

    private String tweetId;
    @NotBlank(message = "User Id cannot be blank")
    private String userId;
    @NotBlank(message = "Tweet text cannot be blank")
    @Size(max = 280, message = "Tweet cannot exceed 280 words")
    private String tweetText;

    private String linkURL;
    private Integer likesCount;
    private List<String> mentions;
    private List<String> hashtags;

    private LocalDateTime createdTimeStamp;
    private LocalDateTime updatedTimeStamp;
    private List<LikeDTO> likes;
    private List<CommentDTO> comments;
    private List<MediaDTO> media;
    private String firstname;
}

