package com.company.addonis.services;

import com.company.addonis.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static com.company.addonis.Helpers.createMockUser;
import static com.company.addonis.services.EmailSenderServiceImpl.SENDER_MAIL;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {

    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    EmailSenderServiceImpl emailSenderService;

    @Test
    public void sendEmail_should_callJavaMailSender() {
        User mockUser = createMockUser();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mockUser.getEmail());
        message.setSubject("String");
        message.setFrom(SENDER_MAIL);
        message.setText("String");
        Mockito.doNothing().when(javaMailSender).send(message);
        emailSenderService.sendEmail(mockUser.getEmail(), "String", "String");
        Mockito.verify(javaMailSender, Mockito.times(1))
                .send(message);

    }
}
