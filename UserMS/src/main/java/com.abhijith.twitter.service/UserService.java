package com.abhijith.twitter.service;

import com.abhijith.twitter.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO findByEmail(String email);
    UserDTO updateUserProfileByMail(String email, UserDTO userDTO);
    UserDTO updateUserProfileById(String id, UserDTO userDTO);
    String login(String email, String password);
    void processForgotPassword(String email);
    UserDTO getUserById(String id);
    List<UserDTO> getAllUsers();
    List<UserDTO> searchUsersByNamePrefix(String prefix);
    void resetPassword(String email, String newPassword);
}
