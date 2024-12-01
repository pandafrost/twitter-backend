package com.abhijith.twitter.controller;

import com.abhijith.twitter.dto.MediaDTO;
import com.abhijith.twitter.service.MediaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/media")
@CrossOrigin(origins = "*") // Adjust as needed
public class MediaAPI {

    @Autowired
    private MediaServiceImpl fileService;

    @PostMapping("/upload")
    public ResponseEntity<List<MediaDTO>> uploadFile(@RequestParam("file") MultipartFile[] file,
                                                     @RequestParam("tweetId") String tweetId,
                                                     @RequestParam("mediaType") String mediaType) {
        try {
            List<MediaDTO> ans = fileService.saveFile(file, tweetId, mediaType);
            return ResponseEntity.ok(ans);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable String id) {
        try {
            fileService.deleteFileById(id);
            return ResponseEntity.ok("File deleted successfully. File ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting file: " + e.getMessage());
        }
    }

    @GetMapping("/{tweetId}")
    public ResponseEntity<List<MediaDTO>> getMedia(@PathVariable String tweetId) {
        List<MediaDTO> res = fileService.getMediaByTweetId(tweetId);
        System.out.println("Results in API ---------------");
        System.out.println(res);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{tweetId}")
    public ResponseEntity<String> deleteByTweetid(@PathVariable String tweetId) {
        fileService.deleteFileByTweetId(tweetId);
        return ResponseEntity.ok("All media associated with tweetId " + tweetId + " has been deleted successfully");
    }

    @GetMapping("/getMedia/{mediaId}")
    public ResponseEntity<Resource> getFile(@PathVariable String mediaId) {
        GridFsResource resource = fileService.getFile(mediaId);
        System.out.println(resource);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

