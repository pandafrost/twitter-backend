package com.abhijith.twitter.controller;

import com.abhijith.twitter.dto.UserDTO;
import com.abhijith.twitter.repository.UserRepository;
import com.abhijith.twitter.service.FileService;
import com.abhijith.twitter.service.UserService;
import jakarta.validation.Valid;
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
@RequestMapping("/users")
public class UserAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserRepository repo;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("This is from UserMS");
    }
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO registeredUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        System.out.println(email);
        UserDTO userDTO = userService.findByEmail(email);
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserDTO> updateUserProfile(@PathVariable String email, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserProfileByMail(email, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/update-profile/{id}")
    public ResponseEntity<UserDTO> updateUserProfileById(@PathVariable String id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserProfileById(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Typically handled by removing the token on the client side.
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        String token = userService.login(email, password);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/{name}")
    public List<UserDTO> searchUsers(@PathVariable String name) {
        return userService.searchUsersByNamePrefix(name);
    }

    @GetMapping("/getImage/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        GridFsResource resource = fileService.getFile(id);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/post-image")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = fileService.saveFile(file);
            return ResponseEntity.ok(fileId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestBody String email) {
        UserDTO user = userService.findByEmail(email);
        return ResponseEntity.ok("User is present");
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String password, @RequestParam String email) {
        userService.resetPassword(email, password);
        return ResponseEntity.ok("Password reset success");
    }
}

