package com.abhijith.twitter.service;

import com.abhijith.twitter.dto.FollowDTO;
import com.abhijith.twitter.dto.UserDTO;

import java.util.List;

public interface FollowService {
    FollowDTO addFollow(FollowDTO followDTO);
    List<UserDTO> getFollowers(String userId);
    List<UserDTO> getFollowing(String userId);
    void deleteFollow(String id);
}

