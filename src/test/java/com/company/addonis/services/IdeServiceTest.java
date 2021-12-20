package com.company.addonis.services;

import com.company.addonis.models.IDE;
import com.company.addonis.repositories.contracts.IdeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.company.addonis.Helpers.createMockIDE;

@ExtendWith(MockitoExtension.class)
public class IdeServiceTest {

    @Mock
    IdeRepository mockRepository;

    @InjectMocks
    IdeServiceImpl ideService;

    @Test
    public void getById_should_callRepository() {
        IDE mockIDE = createMockIDE();
        Mockito.when(mockRepository.getById(mockIDE.getId())).thenReturn(mockIDE);

        ideService.getById(mockIDE.getId());

        Mockito.verify(mockRepository, Mockito.times(1)).getById(mockIDE.getId());
    }

    @Test
    public void getАll_should_ReturnListOfIdes_when_ListOfIdesExist() {
        IDE mockIDE = createMockIDE();
        Mockito.when(mockRepository.getAll()).thenReturn(List.of(mockIDE));

        List<IDE> ides = ideService.getAll();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, ides.size()),
                () -> Assertions.assertEquals(mockIDE.getId(), ides.get(0).getId()),
                () -> Assertions.assertEquals(mockIDE.getName(), ides.get(0).getName()));
    }
}
