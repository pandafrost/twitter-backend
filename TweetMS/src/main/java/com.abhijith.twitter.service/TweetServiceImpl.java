package com.abhijith.twitter.service;

import com.abhijith.twitter.dto.*;
import com.abhijith.twitter.entity.Comment;
import com.abhijith.twitter.entity.Like;
import com.abhijith.twitter.entity.Tweet;
import com.abhijith.twitter.exceptions.TweetException;
import com.abhijith.twitter.repositoty.CommentRepository;
import com.abhijith.twitter.repositoty.LikeRepository;
import com.abhijith.twitter.repositoty.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TweetServiceImpl implements TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private WebClient.Builder wcb;

    @Autowired
    private RestTemplate rst;

    @Override
    public String createTweet(TweetDTO tweetDTO) throws TweetException {
        if (tweetDTO == null) {
            throw new TweetException("Request body cannot be null.");
        }
        if (tweetDTO.getUserId() == null || tweetDTO.getUserId().trim().isEmpty()) {
            throw new TweetException("User ID cannot be null or empty.");
        }
        if (tweetDTO.getTweetText() == null || tweetDTO.getTweetText().trim().isEmpty()) {
            throw new TweetException("Tweet text cannot be null or empty.");
        }

        LocalDateTime today = LocalDateTime.now();
        Tweet tweet = new Tweet();
        tweet.setUserId(tweetDTO.getUserId());
        tweet.setTweetText(tweetDTO.getTweetText());
        tweet.setLinkURL(tweetDTO.getLinkURL());
        tweet.setLikes(tweetDTO.getLikesCount());
        tweet.setHashtags(tweetDTO.getHashtags());
        tweet.setMentions(tweetDTO.getMentions());
        tweet.setCreatedTimeStamp(today);
        tweet.setUpdatedTimeStamp(today);

        Tweet savedTweet = tweetRepository.save(tweet);
        return savedTweet.getId();
    }

    @Override
    public TweetDTO getTweetByTweetId(String tweetId) throws TweetException {
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);

        if (!tweetOptional.isPresent()) {
            throw new TweetException("Tweet not found for ID: " + tweetId);
        }

        TweetDTO tweetDTO = new TweetDTO();

        if (tweetOptional.isPresent()) {
            Tweet tweet = tweetOptional.get();
            tweetDTO.setTweetId(tweet.getId());
            tweetDTO.setUserId(tweet.getUserId());
            tweetDTO.setTweetText(tweet.getTweetText());
            tweetDTO.setLinkURL(tweet.getLinkURL());
            tweetDTO.setLikesCount(tweet.getLikes());
            tweetDTO.setMentions(tweet.getMentions());
            tweetDTO.setHashtags(tweet.getHashtags());
            tweetDTO.setCreatedTimeStamp(tweet.getCreatedTimeStamp());

            List<Like> likes = likeRepository.findByTweetId(tweet.getId());
            List<LikeDTO> likeDTOs = likes.stream()
                    .map(like -> new LikeDTO(like.getId(), like.getTweetId(), like.getUserId()))
                    .collect(Collectors.toList());
            tweetDTO.setLikes(likeDTOs);

            List<Comment> comments = commentRepository.findByTweetId(tweet.getId());
            List<CommentDTO> commentDTOs = comments.stream().map(comment -> new CommentDTO(comment.getId(),
                    comment.getTweetId(), comment.getUserId(), comment.getComment())).collect(Collectors.toList());
            tweetDTO.setComments(commentDTOs);

            String url = "http://localhost:8300/media/" + tweetDTO.getTweetId();
            List<MediaDTO> mediaDTOs = wcb.build().get().uri(url).retrieve().bodyToFlux(MediaDTO.class).collectList().block();
            tweetDTO.setMedia(mediaDTOs);
            return tweetDTO;
        }
        return null;
    }

    @Override
    public List<TweetDTO> getTweetsByUserId(String userId) throws TweetException {
        List<Tweet> tweets = tweetRepository.findByUserId(userId);

        if (tweets.isEmpty()) {
            throw new TweetException("No tweets found for user ID: " + userId);
        }

        List<TweetDTO> tweetDTOs = tweets.stream().map(tweet -> {
            TweetDTO tweetDTO = new TweetDTO();
            tweetDTO.setTweetId(tweet.getId());
            tweetDTO.setUserId(tweet.getUserId());
            tweetDTO.setTweetText(tweet.getTweetText());
            tweetDTO.setLinkURL(tweet.getLinkURL());
            tweetDTO.setLikesCount(tweet.getLikes());
            tweetDTO.setMentions(tweet.getMentions());
            tweetDTO.setHashtags(tweet.getHashtags());

            List<Like> likes = likeRepository.findByTweetId(tweet.getId());
            List<LikeDTO> likeDTOs = likes.stream()
                    .map(like -> new LikeDTO(like.getId(), like.getTweetId(), like.getUserId()))
                    .collect(Collectors.toList());
            tweetDTO.setLikes(likeDTOs);

            List<Comment> comments = commentRepository.findByTweetId(tweet.getId());
            List<CommentDTO> commentDTOs = comments.stream().map(comment -> new CommentDTO(comment.getId(),
                    comment.getTweetId(), comment.getUserId(), comment.getComment())).collect(Collectors.toList());
            tweetDTO.setComments(commentDTOs);

            String url = "http://localhost:8300/media/" + tweetDTO.getTweetId();
            MediaDTO[] mediaDTOs = rst.getForObject(url, MediaDTO[].class);
            List<MediaDTO> medias = Arrays.asList(mediaDTOs);
            tweetDTO.setMedia(medias);

            String url2 = "http://localhost:8100/users/" + tweetDTO.getUserId();
            UserDTO user = wcb.build().get().uri(url2).retrieve().bodyToMono(UserDTO.class).block();
            tweetDTO.setFirstname(user.getFirstname());
            return tweetDTO;
        }).collect(Collectors.toList());

        return tweetDTOs;
    }

    @Override
    public String deleteTweet(String tweetId) throws TweetException {
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);

        if (!tweetOptional.isPresent()) {
            throw new TweetException("Tweet not found for ID: " + tweetId);
        }
        tweetRepository.deleteById(tweetId);
        likeRepository.deleteByTweetId(tweetId);
        commentRepository.deleteByTweetId(tweetId);

        return "Tweet deleted successfully";
    }

    public void changeLikeCount(Integer change, String tweetId) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweetId);
        if (tweetOptional.isPresent()) {
            Tweet tweet = tweetOptional.get();
            tweet.setLikes(tweet.getLikes() + change);
            tweetRepository.save(tweet);
        }
    }

    @Override
    public String likeTweet(String tweetId, LikeDTO likeDTO) throws TweetException {
        if (likeDTO.getTweetId() == null || likeDTO.getUserId() == null) {
            throw new TweetException("Tweet ID and User ID must not be null.");
        }
        Like alreadyLiked = likeRepository.findByTweetIdAndUserId(tweetId, likeDTO.getUserId());
        if (alreadyLiked != null) {
            changeLikeCount(-1, likeDTO.getTweetId());
            return "Already Liked";
        }

        Like like = new Like();
        like.setTweetId(tweetId);
        like.setUserId(likeDTO.getUserId());
        likeRepository.save(like);

        changeLikeCount(1, likeDTO.getTweetId());
        return "Tweet liked successfully";
    }

    @Override
    public String unlikeTweet(String userId, String tweetId) throws TweetException {
        Like like = likeRepository.findByTweetIdAndUserId(tweetId, userId);

        if (like == null) {
            throw new TweetException("Like not found for tweet ID: " + tweetId + " and user ID: " + userId);
        }

        likeRepository.delete(like);
        changeLikeCount(-1, tweetId);
        return "Tweet unliked successfully";
    }

    @Override
    public List<LikeDTO> getTweetLikes(String tweetId) throws TweetException {
        List<Like> likes = likeRepository.findByTweetId(tweetId);

        if (likes.isEmpty()) {
            throw new TweetException("No likes found for tweet ID: " + tweetId);
        }

        return likes.stream()
                .map(like -> new LikeDTO(like.getId(), like.getTweetId(), like.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public String createComment(String tweetId, CommentDTO commentDTO) throws TweetException {
        if (commentDTO.getTweetId() == null || commentDTO.getUserId() == null) {
            throw new TweetException("Tweet ID and User ID must not be null.");
        }

        Comment comment = new Comment();
        comment.setTweetId(tweetId);
        comment.setUserId(commentDTO.getUserId());
        comment.setComment(commentDTO.getComment());

        commentRepository.save(comment);
        return "Comment added successfully";
    }


    @Override
    public String deleteComment(String userId, String tweetId) throws TweetException {
        Comment comment = commentRepository.findByTweetIdAndUserId(tweetId, userId);

        if (comment == null) {
            throw new TweetException("Comment not found for tweet ID: " + tweetId + " and user ID: " + userId);
        }

        commentRepository.delete(comment);
        return "Comment deleted successfully";
    }


    @Override
    public List<CommentDTO> getTweetComments(String tweetId) throws TweetException {
        List<Comment> comments = commentRepository.findByTweetId(tweetId);

        return comments.stream().map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getTweetId(),
                        comment.getUserId(),
                        comment.getComment()))
                .collect(Collectors.toList());
    }


    public List<TweetDTO> getAllTweets() {
        List<Tweet> tweets = tweetRepository.findAll();
        return tweets.stream().map(tweet -> {
            TweetDTO tweetDTO = new TweetDTO();
            tweetDTO.setTweetId(tweet.getId());
            tweetDTO.setUserId(tweet.getUserId());
            tweetDTO.setTweetText(tweet.getTweetText());
            tweetDTO.setLinkURL(tweet.getLinkURL());
            tweetDTO.setLikesCount(tweet.getLikes());
            tweetDTO.setMentions(tweet.getMentions());
            tweetDTO.setHashtags(tweet.getHashtags());

            List<Like> likes = likeRepository.findByTweetId(tweet.getId());
            List<LikeDTO> likeDTOs = likes.stream()
                    .map(like -> new LikeDTO(like.getId(), like.getTweetId(), like.getUserId()))
                    .collect(Collectors.toList());
            tweetDTO.setLikes(likeDTOs);

            List<Comment> comments = commentRepository.findByTweetId(tweet.getId());
            List<CommentDTO> commentDTOs = comments.stream()
                    .map(comment -> new CommentDTO(comment.getId(),
                            comment.getTweetId(),
                            comment.getUserId(),
                            comment.getComment()))
                    .collect(Collectors.toList());
            tweetDTO.setComments(commentDTOs);

            String url = "http://localhost:8300/media/" + tweetDTO.getTweetId();
            MediaDTO[] mediaDTOs = rst.getForObject(url, MediaDTO[].class);
            List<MediaDTO> medias = Arrays.asList(mediaDTOs);
            tweetDTO.setMedia(medias);

            String url2 = "http://localhost:8100/users/" + tweetDTO.getUserId();
            UserDTO user = wcb.build().get().uri(url2).retrieve().bodyToMono(UserDTO.class).block();
            tweetDTO.setFirstname(user.getFirstname());
            return tweetDTO;
        }).collect(Collectors.toList());
    }

}

