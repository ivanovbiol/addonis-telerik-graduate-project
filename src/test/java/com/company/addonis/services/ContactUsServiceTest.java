package com.company.addonis.services;

import com.company.addonis.models.dtos.ContactUsDto;
import com.company.addonis.services.contracts.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.company.addonis.Helpers.createMockContactUsToken;
import static com.company.addonis.services.ContactsUsServiceImpl.*;

@ExtendWith(MockitoExtension.class)
public class ContactUsServiceTest {

    @Mock
    EmailSenderService mockService;

    @InjectMocks
    ContactsUsServiceImpl contactsUsService;

    @Test
    public void contactUs_Should_CallEmailSenderService() {
        ContactUsDto mockContactUsToken = createMockContactUsToken();

        Mockito.doNothing().when(mockService).sendEmail(IVAN_EMAIL, SUBJECT,
                String.format(MAIL_TEXT_TEMPLATE,
                        mockContactUsToken.getYourName(),
                        mockContactUsToken.getYourEmailAddress(),
                        mockContactUsToken.getSubject(),
                        mockContactUsToken.getText()));

        Mockito.doNothing().when(mockService).sendEmail(HRISTIAN_EMAIL, SUBJECT,
                String.format(MAIL_TEXT_TEMPLATE,
                        mockContactUsToken.getYourName(),
                        mockContactUsToken.getYourEmailAddress(),
                        mockContactUsToken.getSubject(),
                        mockContactUsToken.getText()));

        contactsUsService.contactsUs(mockContactUsToken);

        Mockito.verify(mockService, Mockito.times(1)).sendEmail(IVAN_EMAIL, SUBJECT,
                String.format(MAIL_TEXT_TEMPLATE,
                        mockContactUsToken.getYourName(),
                        mockContactUsToken.getYourEmailAddress(),
                        mockContactUsToken.getSubject(),
                        mockContactUsToken.getText()));

        Mockito.verify(mockService, Mockito.times(1)).sendEmail(HRISTIAN_EMAIL, SUBJECT,
                String.format(MAIL_TEXT_TEMPLATE,
                        mockContactUsToken.getYourName(),
                        mockContactUsToken.getYourEmailAddress(),
                        mockContactUsToken.getSubject(),
                        mockContactUsToken.getText()));
    }
}
