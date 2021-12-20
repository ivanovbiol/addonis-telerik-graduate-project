package com.company.addonis.services.contracts;

import com.company.addonis.models.User;

public interface EmailSenderService {

    void sendEmail(User user);

    void sendEmail(String receiver, String subject, String text);

    void inviteFriend(String friendEmail);
}
