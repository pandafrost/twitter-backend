package com.abhijith.twitter.controller;

import com.abhijith.twitter.dto.FollowDTO;
import com.abhijith.twitter.dto.UserDTO;
import com.abhijith.twitter.entity.Follow;
import com.abhijith.twitter.repository.FollowRepository;
import com.abhijith.twitter.service.FollowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/follows")
public class FollowAPI {

    @Autowired
    private FollowServiceImpl followService;

    @Autowired
    private FollowRepository followRepository;

    @GetMapping("/health")
    public ResponseEntity<String> testing() {
        return ResponseEntity.ok("Hi, this is followMS");
    }

    @PostMapping("/post")
    public ResponseEntity<FollowDTO> addFollow(@RequestBody FollowDTO followDTO) {
        FollowDTO resultDTO = followService.addFollow(followDTO);
        return ResponseEntity.ok(resultDTO);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable String userId) {
        List<UserDTO> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/getFollowers/{userId}")
    public ResponseEntity<List<FollowDTO>> getAllFollowers(@PathVariable String userId) {
        List<Follow> li = followRepository.findByFollowingId(userId);
        List<FollowDTO> result = new ArrayList<>();
        for (Follow it : li) {
            FollowDTO f = new FollowDTO();
            f.setFollowerId(it.getFollowerId());
            f.setFollowingId(it.getFollowingId());
            f.setId(it.getId());
            result.add(f);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<UserDTO>> getFollowing(@PathVariable String userId) {
        List<UserDTO> following = followService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/getFollowing/{userId}")
    public ResponseEntity<List<FollowDTO>> getAllFollowing(@PathVariable String userId) {
        List<Follow> li = followRepository.findByFollowerId(userId);
        List<FollowDTO> result = new ArrayList<>();
        for (Follow it : li) {
            FollowDTO f = new FollowDTO();
            f.setFollowerId(it.getFollowerId());
            f.setFollowingId(it.getFollowingId());
            f.setId(it.getId());
            result.add(f);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFollowById(@PathVariable String id) {
        followService.deleteFollow(id);
        return ResponseEntity.ok("Follow Deleted successfully");
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteFollow(@RequestParam String followerId, @RequestParam String followingId) {
        Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);

        if (existingFollow.isPresent()) {
            Follow follow = existingFollow.get();
            followRepository.deleteById(follow.getId());
        }

        return ResponseEntity.ok("Follow Deleted successfully");
    }
}

