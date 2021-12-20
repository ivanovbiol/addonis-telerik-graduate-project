package com.company.addonis.services;

import com.company.addonis.models.dtos.ContactUsDto;
import com.company.addonis.services.contracts.ContactsUsService;
import com.company.addonis.services.contracts.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactsUsServiceImpl implements ContactsUsService {

    public static final String MAIL_TEXT_TEMPLATE = "%s with %s email is reaching you about %s%n%n%s";
    public static final String SUBJECT = "Email form Addonis application";
    public static final String IVAN_EMAIL = "ivanov.biol@gmail.com";
    public static final String HRISTIAN_EMAIL = "hristian.ivanov100@gmail.com";

    private final EmailSenderService emailSenderService;

    @Autowired
    public ContactsUsServiceImpl(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void contactsUs(ContactUsDto contactUsDto) {
        String text = String.format(MAIL_TEXT_TEMPLATE,
                contactUsDto.getYourName(),
                contactUsDto.getYourEmailAddress(),
                contactUsDto.getSubject(), contactUsDto.getText());
        emailSenderService.sendEmail(IVAN_EMAIL, SUBJECT, text);
        emailSenderService.sendEmail(HRISTIAN_EMAIL, SUBJECT, text);
    }
}
