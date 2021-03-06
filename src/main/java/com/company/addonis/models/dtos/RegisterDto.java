package com.company.addonis.models.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterDto extends LoginDto {

    public static final String PASSWORD_CONFIRMATION_INVALID =
            "Password confirmation must match password.";

    @NotEmpty(message = PASSWORD_CONFIRMATION_INVALID)
    private String passwordConfirm;

    @NotEmpty(message = "Email can not be empty.")
    @Email(message = "Please enter valid email.")
    @Pattern(regexp = ".+(@gmail\\.com|@abv\\.bg)",
            message = "Email options restricted to @gmail.com or @abv.bg.")
    private String email;

    @Size(min = 10, max = 10, message = "Phone number must have exactly 10 numbers!")
    private String phoneNumber;

    public RegisterDto() {
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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
