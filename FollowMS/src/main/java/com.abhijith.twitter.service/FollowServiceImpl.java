package com.abhijith.twitter.service;

import com.abhijith.twitter.dto.FollowDTO;
import com.abhijith.twitter.dto.UserDTO;
import com.abhijith.twitter.entity.Follow;
import com.abhijith.twitter.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private WebClient.Builder wbc;

    @Override
    public FollowDTO addFollow(FollowDTO followDTO) {
        Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowingId(followDTO.getFollowerId(),
                followDTO.getFollowingId());

        if (existingFollow.isPresent()) {
            throw new RuntimeException("Follow relationship already exists between follower ID: "
                    + followDTO.getFollowerId() + " and following ID: " + followDTO.getFollowingId());
        }

        if (followDTO.getFollowerId().equals(followDTO.getFollowingId())) {
            throw new RuntimeException("User cannot follow himself/herself");
        }

        Follow follow = new Follow();
        follow.setFollowerId(followDTO.getFollowerId());
        follow.setFollowingId(followDTO.getFollowingId());
        Follow savedFollow = followRepository.save(follow);

        FollowDTO resultDTO = new FollowDTO();
        resultDTO.setId(savedFollow.getId());
        resultDTO.setFollowerId(savedFollow.getFollowerId());
        resultDTO.setFollowingId(savedFollow.getFollowingId());
        return resultDTO;
    }

    private final String USER_MS_BASE_URL = "http://localhost:4000/users/";

    @Override
    public List<UserDTO> getFollowers(String userId) {
        List<Follow> followers = followRepository.findByFollowingId(userId);
        List<String> followerIds = followers.stream().map(Follow::getFollowerId).collect(Collectors.toList());
        Flux<UserDTO> followerFlux = Flux.fromIterable(followerIds)
                .flatMap(followerId -> wbc.build().get()
                        .uri(USER_MS_BASE_URL + followerId)
                        .retrieve()
                        .bodyToMono(UserDTO.class));
        return followerFlux.collectList().block();
    }

    @Override
    public List<UserDTO> getFollowing(String userId) {
        List<Follow> followings = followRepository.findByFollowerId(userId);
        List<String> followingIds = followings.stream().map(Follow::getFollowingId).collect(Collectors.toList());
        Flux<UserDTO> followingFlux = Flux.fromIterable(followingIds)
                .flatMap(followingId -> wbc.build().get()
                        .uri(USER_MS_BASE_URL + followingId)
                        .retrieve()
                        .bodyToMono(UserDTO.class));
        return followingFlux.collectList().block();
    }

    @Override
    public void deleteFollow(String id) {
        followRepository.deleteById(id);
    }
}

