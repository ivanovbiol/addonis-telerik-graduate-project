package com.company.addonis.models.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {

    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols.")
    private String username;

    @Size(min = 8, message = "Password must have at least 8 symbols.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$", message = "password must be at least 8 characters long " +
            "with 1 uppercase, 1 lowercase 1, special character & 1 numeric character.")
    private String password;

    @Email(message = "Please enter valid email.")
    private String email;

    @Size(min = 10, max = 10, message = "Phone number must have 10 numbers!")
    private String phoneNumber;

    public UserDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}

