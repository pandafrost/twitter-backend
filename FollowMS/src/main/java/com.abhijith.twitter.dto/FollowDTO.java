package com.abhijith.twitter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FollowDTO {
    private String id;

    @NotBlank(message = "Follower ID is mandatory")
    private String followerId;

    @NotBlank(message = "Following ID is mandatory")
    private String followingId;
}

