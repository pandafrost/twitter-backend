package com.abhijith.twitter.service;

import com.abhijith.twitter.dto.UserDTO;
import com.abhijith.twitter.entity.PasswordResetToken;
import com.abhijith.twitter.entity.User;
import com.abhijith.twitter.repository.PasswordResetTokenRepository;
import com.abhijith.twitter.repository.UserRepository;
import com.abhijith.twitter.utility.TokenGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements  UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenGenerator tokenGenerator;

//    @Autowired
//    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FileService fileService;

    @Autowired
    private MongoService mongoService;

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public UserDTO registerUser(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("A user with this email already exists.");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstname());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(user);

        UserDTO result = new UserDTO();
        result.setUserId(savedUser.getId());
        return result;
    }

    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }
        return null;
    }

    public UserDTO updateUserProfileByMail(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setFirstname(userDTO.getFirstname());
            user.setEmail(userDTO.getEmail());
            user.setProfilePic(userDTO.getProfilePic());
            user.setCoverPic(userDTO.getCoverPic());
            user.setBio(userDTO.getBio());
            user.setLocation(userDTO.getLocation());
            user.setWebsite(userDTO.getWebsite());
            user.setPrivate(userDTO.isPrivate());
            User updatedUser = userRepository.save(user);
            UserDTO updatedUserDTO = new UserDTO();
            updatedUserDTO.setUserId(updatedUser.getId());
            updatedUserDTO.setFirstname(updatedUser.getFirstname());
            updatedUserDTO.setEmail(updatedUser.getEmail());
            updatedUserDTO.setPassword(updatedUser.getPassword());
            updatedUserDTO.setProfilePic(updatedUser.getProfilePic());
            updatedUserDTO.setCoverPic(updatedUser.getCoverPic());
            updatedUserDTO.setBio(updatedUser.getBio());
            updatedUserDTO.setLocation(updatedUser.getLocation());
            updatedUserDTO.setWebsite(updatedUser.getWebsite());
            updatedUserDTO.setPrivate(updatedUser.isPrivate());
            return updatedUserDTO;
        }
        return null;
    }

    public UserDTO updateUserProfileById(String id, UserDTO userDTO) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            BeanUtils.copyProperties(userDTO, user, "id", "password");
            User updatedUser = userRepository.save(user.get());
            UserDTO updatedUserDTO = new UserDTO();
            BeanUtils.copyProperties(updatedUser, updatedUserDTO);
            return updatedUserDTO;
        }
        return null;
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            return jwtUtil.generateToken(user.getId());
            return user.getId();
        }
        throw new RuntimeException("Invalid email or password");
    }

    public void processForgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String token = tokenGenerator.generateToken();
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
            passwordResetTokenRepository.save(passwordResetToken);

            String resetUrl = "http://your-app-url.com/reset-password?token=" + token;
            emailService.sendSimpleMessage(user.getEmail(), "Password Reset Request",
                    "To reset your password, click the link below:\n" + resetUrl);
        }
    }

    public UserDTO getUserById(String id) {
        User user = mongoService.findById(id);
        UserDTO result = convertToDTO(user);
        result.setUserId(user.getId());
        return result;
    }

    public List<UserDTO> getAllUsers() {
        List<User> allUser = userRepository.findAll();

        List<UserDTO> result = new ArrayList<>();
        for (User it : allUser) {
            UserDTO user = convertToDTO(it);
            user.setUserId(it.getId());
            result.add(user);
        }
        return result;
    }

    public List<UserDTO> searchUsersByNamePrefix(String prefix) {
        List<User> users = userRepository.findByFirstnameStartingWith(prefix);
        List<UserDTO> result = new ArrayList<>();
        for (User it : users) {
            UserDTO user = convertToDTO(it);
            user.setUserId(it.getId());
            result.add(user);
        }
        return result;
    }

    public void resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}

