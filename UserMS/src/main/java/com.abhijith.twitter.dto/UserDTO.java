package com.abhijith.twitter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    private String userId;

    @NotBlank(message = "First name is mandatory")
    @Size(max = 100, message = "First name can have a maximum of 100 characters")
    private String firstname;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;

    private String profilePic;
    private String coverPic;
    private String bio;
    private String location;
    private String website;

    private boolean isPrivate;
}

