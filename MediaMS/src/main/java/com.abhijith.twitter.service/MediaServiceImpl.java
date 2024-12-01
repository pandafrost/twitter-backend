package com.abhijith.twitter.service;

import com.abhijith.twitter.dto.MediaDTO;
import com.abhijith.twitter.entity.Media;
import com.abhijith.twitter.repository.MediaRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MediaServiceImpl implements MediaService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MediaRepository mediaRepo;

    public List<MediaDTO> saveFile(MultipartFile[] files, String tweetId, String mediaType) throws IOException {
        List<MediaDTO> res = new ArrayList<>();
        for (MultipartFile it : files) {
            try {
                ObjectId id = gridFsTemplate.store(it.getInputStream(), it.getOriginalFilename(), it.getContentType());
                String url = id.toString();
                Media md = new Media();
                md.setTweetId(tweetId);
                md.setMediaUrl(url);
                Media saved = mediaRepo.save(md);

                MediaDTO mediaDTO = new MediaDTO();
                mediaDTO.setMediaUrl(saved.getMediaUrl());
                mediaDTO.setMediaId(saved.getId());
                mediaDTO.setTweetId(saved.getTweetId());

                res.add(mediaDTO);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save Media", e);
            }
        }
        return res;
    }

    public void deleteFileById(String id) {
        ObjectId objectId = new ObjectId(id);
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(objectId)));
    }

    public List<MediaDTO> getMediaByTweetId(String tweetID) {
        List<Media> md = mediaRepo.findByTweetId(tweetID);
        List<MediaDTO> ans = new ArrayList<>();
        for (Media it : md) {
            MediaDTO res = new MediaDTO();
            res.setMediaId(it.getId());
            res.setTweetId(it.getTweetId());
            res.setMediaUrl(it.getMediaUrl());
            ans.add(res);
            System.out.println("-----------------------------");
            System.out.println(res);
        }
        System.out.println("-----------------------------");
        System.out.println(ans);
        return ans;
    }

    public void deleteFileByTweetId(String tweetId) {
        List<Media> mediaList = mediaRepo.findByTweetId(tweetId);
        if (mediaList.isEmpty()) {
            throw new RuntimeException("No media found for tweetId: " + tweetId);
        }

        for (Media it : mediaList) {
            try {
                GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(new ObjectId(it.getId()))));
                if (file != null) {
                    gridFsTemplate.delete(Query.query(Criteria.where("_id").is(new ObjectId(it.getId()))));
                }
                mediaRepo.deleteByTweetId(it.getTweetId());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Failed to delete media for id: " + it.getId());
            }
        }
    }

    public GridFsResource getFile(String id) {
        ObjectId objectId;
        System.out.println("========================================");
        System.out.println(id);
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ID format");
        }
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(objectId)));
        if (file != null) {
            return gridFsTemplate.getResource(file);
        }
        return null;
    }
}

