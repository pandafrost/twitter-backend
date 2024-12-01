package com.abhijith.twitter.service;

import com.abhijith.twitter.dto.MediaDTO;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService{
    List<MediaDTO> saveFile(MultipartFile[] files, String tweetId, String mediaType) throws IOException;
    void deleteFileById(String id);
    List<MediaDTO> getMediaByTweetId(String tweetID);
    void deleteFileByTweetId(String tweetId);
    GridFsResource getFile(String id);
}

