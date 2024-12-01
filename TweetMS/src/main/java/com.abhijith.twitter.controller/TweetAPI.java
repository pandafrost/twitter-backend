package com.abhijith.twitter.controller;

import com.abhijith.twitter.dto.CommentDTO;
import com.abhijith.twitter.dto.LikeDTO;
import com.abhijith.twitter.dto.TweetDTO;
import com.abhijith.twitter.exceptions.TweetException;
import com.abhijith.twitter.repositoty.TweetRepository;
import com.abhijith.twitter.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/tweets")
public class TweetAPI {

    @Autowired
    public TweetService tweetService;

    @Autowired
    public TweetRepository repo;

    @GetMapping("/health")
    public ResponseEntity<String> check() {
        return new ResponseEntity<>("Hello, this is tweets service", HttpStatus.OK);
    }

    @PostMapping("/create-tweet")
    public ResponseEntity<String> postCreation(@RequestBody TweetDTO tweet) throws TweetException {
        String tweetId = tweetService.createTweet(tweet);
        return new ResponseEntity<>(tweetId, HttpStatus.CREATED);
    }

    @GetMapping("tweet/{tweetId}")
    public ResponseEntity<TweetDTO> getTweetById(@PathVariable String tweetId) throws TweetException {
        TweetDTO tweet = tweetService.getTweetByTweetId(tweetId);
        if (tweet == null) {
            return null;
        }
        return new ResponseEntity<>(tweet, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<TweetDTO>> getTweetsByUserId(@PathVariable String userId) throws TweetException {
        List<TweetDTO> tweets = tweetService.getTweetsByUserId(userId);
        if (tweets.isEmpty()) {
            return null;
        }
        return new ResponseEntity<>(tweets, HttpStatus.OK);
    }

    @PostMapping("/{tweetId}/like")
    public ResponseEntity<String> likeTweet(@PathVariable String tweetId, @RequestBody LikeDTO likeDTO) throws TweetException {
        String response = tweetService.likeTweet(tweetId, likeDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{tweetId}/like")
    public ResponseEntity<String> unlikeTweet(@PathVariable String tweetId, @RequestParam String userId) throws TweetException {
        String response = tweetService.unlikeTweet(userId, tweetId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{tweetId}/comment")
    public ResponseEntity<String> addComment(@PathVariable String tweetId, @RequestBody CommentDTO commentDTO) throws TweetException {
        String response = tweetService.createComment(tweetId, commentDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{tweetId}/comment")
    public ResponseEntity<String> deleteComment(@RequestParam String userId, @PathVariable String tweetId) throws TweetException {
        String response = tweetService.deleteComment(userId, tweetId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{tweetId}/likes")
    public ResponseEntity<List<LikeDTO>> getTweetLikes(@PathVariable String tweetId) throws TweetException {
        List<LikeDTO> likes = tweetService.getTweetLikes(tweetId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @GetMapping("/{tweetId}/comment")
    public ResponseEntity<List<CommentDTO>> getTweetComments(@PathVariable String tweetId) throws TweetException {
        List<CommentDTO> comments = tweetService.getTweetComments(tweetId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{tweetId}")
    public ResponseEntity<String> deleteTweet(@PathVariable String tweetId) throws TweetException {
        String response = tweetService.deleteTweet(tweetId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<TweetDTO>> getTweetsForUserAndFollowing() throws TweetException {
        List<TweetDTO> tweets = tweetService.getAllTweets();
        return new ResponseEntity<>(tweets, HttpStatus.OK);
    }
}

