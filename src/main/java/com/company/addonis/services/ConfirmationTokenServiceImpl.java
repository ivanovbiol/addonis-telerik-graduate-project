package com.company.addonis.services;

import com.company.addonis.exceptions.UnauthorizedOperationException;
import com.company.addonis.models.ConfirmationToken;
import com.company.addonis.repositories.contracts.ConfirmationTokenRepository;
import com.company.addonis.services.contracts.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public ConfirmationToken getByField(String token) {
        ConfirmationToken tokenFromBase = confirmationTokenRepository.getByField("token", token);
        if (tokenFromBase.getExpirationDate().isBefore(LocalDate.now())) {
            throw new UnauthorizedOperationException("Your confirmation email has expired - " +
                    "please contact Admin");
        }
        return tokenFromBase;
    }

    @Override
    public void create(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.create(confirmationToken);
    }
}
