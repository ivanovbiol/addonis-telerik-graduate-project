package com.company.addonis.services;

import com.company.addonis.models.Tag;
import com.company.addonis.repositories.contracts.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.company.addonis.Helpers.createMockTag;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    TagRepository mockRepository;

    @InjectMocks
    TagServiceImpl tagService;

    @Test
    public void getById_should_CallRepository() {
        Tag mockTag = createMockTag();
        Mockito.when(mockRepository.getById(mockTag.getId())).thenReturn(mockTag);

        tagService.getById(mockTag.getId());

        Mockito.verify(mockRepository, Mockito.times(1)).getById(mockTag.getId());
    }

    @Test
    public void getАll_should_ReturnListOfTags_when_ListOfTagsExist() {
        Tag mockTag = createMockTag();
        Mockito.when(mockRepository.getAll()).thenReturn(List.of(mockTag));

        List<Tag> resultTags = tagService.getAll();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, resultTags.size()),
                () -> Assertions.assertEquals(mockTag.getId(), resultTags.get(0).getId()),
                () -> Assertions.assertEquals(mockTag.getName(), resultTags.get(0).getName()));
    }

    @Test
    public void getByValue_should_ReturnTag_when_TagExist() {
        Tag mockTag = createMockTag();
        Mockito.when(mockRepository.getByValue(mockTag.getName())).thenReturn(mockTag);

        Tag resultTag = tagService.getByValue(mockTag.getName());

        Assertions.assertAll(
                () -> Assertions.assertEquals(mockTag.getId(), resultTag.getId()),
                () -> Assertions.assertEquals(mockTag.getName(), resultTag.getName()));
    }

    @Test
    public void create_should_CallRepository() {
        Tag mockTag = createMockTag();
        tagService.create(mockTag);

        Mockito.verify(mockRepository, Mockito.times(1)).create(mockTag);
    }
}
