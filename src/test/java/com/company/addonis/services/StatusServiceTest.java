package com.company.addonis.services;

import com.company.addonis.models.Status;
import com.company.addonis.repositories.contracts.StatusRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.company.addonis.Helpers.createMockStatus;

@ExtendWith(MockitoExtension.class)
public class StatusServiceTest {

    @Mock
    StatusRepository mockRepository;

    @InjectMocks
    StatusServiceImpl statusService;

    @Test
    public void getById_should_callRepository() {
        Status mockStatus = createMockStatus();
        Mockito.when(mockRepository.getById(mockStatus.getId())).thenReturn(mockStatus);

        Status resultStatus = statusService.getById(mockStatus.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(mockStatus.getId(), resultStatus.getId()),
                () -> Assertions.assertEquals(mockStatus.getName(), resultStatus.getName()));
    }
}
