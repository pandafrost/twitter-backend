package com.abhijith.twitter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {

    private String mediaId;
    @NotBlank(message = "Tweet id cannot be blank")
    private String tweetId;

    @NotBlank(message = "MediaUrl cannot be blank")
    private String mediaUrl;
}

