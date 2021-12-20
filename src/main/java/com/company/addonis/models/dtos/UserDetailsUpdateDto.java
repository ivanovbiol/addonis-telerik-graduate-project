package com.company.addonis.models.dtos;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class UserDetailsUpdateDto {

    @Email(message = "Please enter valid email.")
    private String email;

    @Size(min = 10, max = 10, message = "Phone number must have 10 numbers!")
    private String phoneNumber;

    private MultipartFile photo;

    public UserDetailsUpdateDto() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
