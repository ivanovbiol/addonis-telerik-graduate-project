package com.company.addonis.models.dtos;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginDto {

    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols.")
    private String username;

    @Size(min = 8, message = "Password must have at least 8 symbols.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$", message =
            "Required: 1 uppercase, 1 lowercase 1, special symbol & 1 numeric.")
    private String password;

    public LoginDto() {
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
}
