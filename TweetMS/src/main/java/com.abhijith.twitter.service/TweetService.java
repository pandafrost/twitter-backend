package com.abhijith.twitter.service;

import com.abhijith.twitter.dto.CommentDTO;
import com.abhijith.twitter.dto.LikeDTO;
import com.abhijith.twitter.dto.TweetDTO;
import com.abhijith.twitter.exceptions.TweetException;

import java.util.List;

public interface TweetService {
    String createTweet(TweetDTO tweetDTO) throws TweetException;
    TweetDTO getTweetByTweetId(String tweetId) throws TweetException;
    List<TweetDTO> getTweetsByUserId(String userId) throws TweetException;
    String deleteTweet(String tweetId) throws TweetException;

    String likeTweet(String tweetId, LikeDTO likeDTO) throws TweetException;
    String unlikeTweet(String userId, String tweetId) throws TweetException;
    List<LikeDTO> getTweetLikes(String tweetId) throws TweetException;

    String createComment(String tweetId, CommentDTO commentDTO) throws TweetException;
    String deleteComment(String userId, String tweetId) throws TweetException;
    List<CommentDTO> getTweetComments(String tweetId) throws TweetException;
    List<TweetDTO> getAllTweets() throws TweetException;
}


