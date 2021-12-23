package com.company.addonis.models.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ContactUsDto {

    @NotBlank(message = "Your name can not be blank.")
    @Size(min = 3, message = "Your name should be at lest 3 symbols.")
    private String yourName;

    @NotBlank(message = "Your email can not be blank.")
    @Size(min = 5, max = 25, message = "Your email should be between 5 and 25 symbols.")
    private String yourEmailAddress;

    @NotBlank(message = "Subject can not be blank.")
    @Size(min = 5, max = 25, message = "Subject should be between 5 and 25 symbols.")
    private String subject;

    @NotBlank(message = "Message can not be blank.")
    @Size(min = 10, message = "Message should be at lest 10 symbols.")
    private String text;

    public ContactUsDto(){}

    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    public String getYourEmailAddress() {
        return yourEmailAddress;
    }

    public void setYourEmailAddress(String yourEmailAddress) {
        this.yourEmailAddress = yourEmailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
