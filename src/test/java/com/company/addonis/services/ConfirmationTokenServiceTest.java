package com.company.addonis.services;

import com.company.addonis.exceptions.UnauthorizedOperationException;
import com.company.addonis.models.ConfirmationToken;
import com.company.addonis.repositories.contracts.ConfirmationTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.company.addonis.Helpers.createMockConfirmationToken;

@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenServiceTest {

    @Mock
    ConfirmationTokenRepository mockRepository;

    @InjectMocks
    ConfirmationTokenServiceImpl confirmationTokenService;

    @Test
    public void getByField_should_CallRepository() {
        ConfirmationToken mockToken = createMockConfirmationToken();
        Mockito.when(mockRepository.getByField("token", mockToken.getToken())).thenReturn(mockToken);

        confirmationTokenService.getByField(mockToken.getToken());

        Mockito.verify(mockRepository, Mockito.times(1)).getByField("token", mockToken.getToken());
    }

    @Test
    public void getByField_should_Throw_when_TokenExpirationDateHasExpired() {
        ConfirmationToken mockToken = createMockConfirmationToken();
        mockToken.setExpirationDate(LocalDate.now().minusDays(1));
        Mockito.when(mockRepository.getByField("token", mockToken.getToken())).thenReturn(mockToken);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> confirmationTokenService.getByField(mockToken.getToken()));
    }

    @Test
    public void create_Should_CallRepository() {
        ConfirmationToken confirmationToken = createMockConfirmationToken();
        confirmationTokenService.create(confirmationToken);
        Mockito.verify(mockRepository, Mockito.times(1)).create(confirmationToken);
    }
}
