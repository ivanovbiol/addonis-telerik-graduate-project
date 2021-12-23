package com.company.addonis.models.dtos;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserPasswordChangeDto {

    @Size(min = 8, message = "Password must have at least 8 symbols.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$",
            message = "Password must be at least 8 characters long " +
                    "with 1 uppercase, 1 lowercase 1, special character & 1 numeric character.")
    private String oldPassword;

    @Size(min = 8, message = "Password must have at least 8 symbols.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$",
            message = "Password must be at least 8 characters long " +
                    "with 1 uppercase, 1 lowercase 1, special character & 1 numeric character.")
    private String newPassword;

    @Size(min = 8, message = "Password must have at least 8 symbols.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$",
            message = "Password must be at least 8 characters long " +
                    "with 1 uppercase, 1 lowercase 1, special character & 1 numeric character.")
    private String confirmNewPassword;

    public UserPasswordChangeDto() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
