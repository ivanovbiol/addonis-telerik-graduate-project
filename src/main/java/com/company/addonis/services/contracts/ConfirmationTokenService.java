package com.company.addonis.services.contracts;

import com.company.addonis.models.ConfirmationToken;

public interface ConfirmationTokenService {

    ConfirmationToken getByField(String token);

    void create(ConfirmationToken confirmationToken);

}
