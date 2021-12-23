package com.company.addonis.services;

import com.company.addonis.exceptions.DuplicateEntityException;
import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.models.Addon;
import com.company.addonis.repositories.contracts.AddonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.company.addonis.Helpers.createMockAddon;
import static com.company.addonis.utils.ObjectFieldsSetter.*;

@ExtendWith(MockitoExtension.class)
public class AddonServiceTest {

    @Mock
    AddonRepository mockRepository;

    @InjectMocks
    AddonServiceImpl addonService;

    @Test
    public void getAll_should_CallRepository() {
        Addon mockAddon = createMockAddon();
        Mockito.when(mockRepository.getAll()).thenReturn(List.of(mockAddon));

        addonService.getAll();

        Mockito.verify(mockRepository, Mockito.times(1)).getAll();
    }

    @Test
    public void getById_should_ReturnAddon_when_AddonExists() {
        Addon mockAddon = createMockAddon();
        Mockito.when(mockRepository.getById(mockAddon.getId())).thenReturn(mockAddon);
        Addon resultAddon = addonService.getById(mockAddon.getId());
        setStaticMockAddonFields(mockAddon);

        Assertions.assertAll(
                () -> Assertions.assertEquals(mockAddon.getId(), resultAddon.getId()),
                () -> Assertions.assertEquals(mockAddon.getName(), resultAddon.getName()),
                () -> Assertions.assertEquals(mockAddon.getCreator().getId(), resultAddon.getCreator().getId()),
                () -> Assertions.assertEquals(mockAddon.getImage(), resultAddon.getImage()),
                () -> Assertions.assertEquals(mockAddon.getImageString(), resultAddon.getImageString()),
                () -> Assertions.assertEquals(mockAddon.getBinaryContent(), resultAddon.getBinaryContent()),
                () -> Assertions.assertEquals(mockAddon.getCalculatedScore(), resultAddon.getCalculatedScore()),
                () -> Assertions.assertEquals(mockAddon.getCreator().getPhoto(), resultAddon.getCreator().getPhoto()),
                () -> Assertions.assertEquals(mockAddon.getCreator().getImage(), resultAddon.getCreator().getImage()));
    }

    @Test
    public void create_should_CallRepository() {
        Addon mockAddon = createMockAddon();
        Mockito.when(mockRepository.getByField("name", mockAddon.getName())).thenThrow(EntityNotFoundException.class);

        addonService.create(mockAddon);

        Mockito.verify(mockRepository, Mockito.times(1)).create(mockAddon);
    }

    @Test
    public void create_should_Throw_when_AddonAlreadyExists() {
        Addon mockAddon = createMockAddon();
        Addon mockAddonSecond = createMockAddon();
        mockAddonSecond.setId(111);
        Mockito.when(mockRepository.getByField("name", mockAddon.getName())).thenReturn(mockAddonSecond);

        Assertions.assertThrows(DuplicateEntityException.class, () -> addonService.create(mockAddon));
    }

    @Test
    public void create_should_CallRepository_when_AddonDoesNotExists() {
        Addon mockAddon = createMockAddon();
        Mockito.when(mockRepository.getByField("name", mockAddon.getName())).thenReturn(mockAddon);

        addonService.create(mockAddon);
        Mockito.verify(mockRepository, Mockito.times(1)).create(mockAddon);
    }

    @Test
    public void update_should_CallRepository() {
        Addon mockAddon = createMockAddon();
        Mockito.when(mockRepository.getByField("name", mockAddon.getName())).thenThrow(EntityNotFoundException.class);

        addonService.update(mockAddon);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockAddon);
    }

    @Test
    public void update_should_Throw_when_AddonAlreadyExists() {
        Addon mockAddon = createMockAddon();
        Addon mockAddonSecond = createMockAddon();
        mockAddonSecond.setId(111);
        Mockito.when(mockRepository.getByField("name", mockAddon.getName())).thenReturn(mockAddonSecond);

        Assertions.assertThrows(DuplicateEntityException.class, () -> addonService.update(mockAddon));
    }

    @Test
    public void update_should_CallRepository_when_AddonDoesNotExists() {
        Addon mockAddon = createMockAddon();
        Mockito.when(mockRepository.getByField("name", mockAddon.getName())).thenReturn(mockAddon);

        addonService.update(mockAddon);
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockAddon);
    }

    @Test
    public void delete_should_CallRepository() {
        Addon mockAddon = createMockAddon();
        addonService.delete(mockAddon.getId());

        Mockito.verify(mockRepository, Mockito.times(1)).delete(mockAddon.getId());
    }

    @Test
    public void filter_should_ReturnListOfAddons_when_AddonsExist() {
        Addon mockAddon = createMockAddon();
        Mockito.when(mockRepository.filter(
                        Optional.of(mockAddon.getName()),
                        Optional.of(mockAddon.getIde().getName()),
                        Optional.of("true"),
                        Optional.of(mockAddon.getCreator().getUsername()),
                        Optional.of("sortBy")))
                .thenReturn(List.of(mockAddon));

        List<Addon> resultList = addonService.filter(
                Optional.of(mockAddon.getName()),
                Optional.of(mockAddon.getIde().getName()),
                Optional.of("true"),
                Optional.of(mockAddon.getCreator().getUsername()),
                Optional.of("sortBy"));

        List<Addon> expectedList = List.of(mockAddon);
        expectedList.forEach(this::setStaticMockAddonFields);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedList.size(), resultList.size()),
                () -> Assertions.assertEquals(expectedList.get(0).getId(), resultList.get(0).getId()),
                () -> Assertions.assertEquals(expectedList.get(0).getName(), resultList.get(0).getName()),
                () -> Assertions.assertEquals(expectedList.get(0).getImage(), resultList.get(0).getImage()),
                () -> Assertions.assertEquals(expectedList.get(0).getImageString(), resultList.get(0).getImageString()),
                () -> Assertions.assertEquals(expectedList.get(0).getBinaryContent(), resultList.get(0).getBinaryContent()),
                () -> Assertions.assertEquals(expectedList.get(0).getCreator().getPhoto(), resultList.get(0).getCreator().getPhoto()),
                () -> Assertions.assertEquals(expectedList.get(0).getCreator().getImage(), resultList.get(0).getCreator().getImage()),
                () -> Assertions.assertEquals(expectedList.get(0).getCalculatedScore(), resultList.get(0).getCalculatedScore()));
    }

    @Test
    public void getByColumn_should_ReturnListOfAddons_when_AddonsExist() {
        Addon mockAddon = createMockAddon();
        Mockito.when(mockRepository.getByColumn("name", 1)).thenReturn(List.of(mockAddon));

        List<Addon> resultList = addonService.getByColumn("name", 1);

        List<Addon> expectedList = List.of(mockAddon);
        expectedList.forEach(this::setStaticMockAddonFields);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedList.size(), resultList.size()),
                () -> Assertions.assertEquals(expectedList.get(0).getId(), resultList.get(0).getId()),
                () -> Assertions.assertEquals(expectedList.get(0).getName(), resultList.get(0).getName()),
                () -> Assertions.assertEquals(expectedList.get(0).getImage(), resultList.get(0).getImage()),
                () -> Assertions.assertEquals(expectedList.get(0).getImageString(), resultList.get(0).getImageString()),
                () -> Assertions.assertEquals(expectedList.get(0).getBinaryContent(), resultList.get(0).getBinaryContent()),
                () -> Assertions.assertEquals(expectedList.get(0).getCreator().getPhoto(), resultList.get(0).getCreator().getPhoto()),
                () -> Assertions.assertEquals(expectedList.get(0).getCreator().getImage(), resultList.get(0).getCreator().getImage()),
                () -> Assertions.assertEquals(expectedList.get(0).getCalculatedScore(), resultList.get(0).getCalculatedScore()));
    }

    @Test
    public void getAllAddonsByUser_should_ReturnListOfAddons_when_AddonsExist() {
        Addon mockAddon = createMockAddon();
        Mockito.when(mockRepository.getAddonsByUser(mockAddon.getCreator().getId())).thenReturn(List.of(mockAddon));

        List<Addon> resultList = addonService.getAddonsByUser(mockAddon.getCreator().getId());

        List<Addon> expectedList = List.of(mockAddon);
        expectedList.forEach(this::setStaticMockAddonFields);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedList.size(), resultList.size()),
                () -> Assertions.assertEquals(expectedList.get(0).getId(), resultList.get(0).getId()),
                () -> Assertions.assertEquals(expectedList.get(0).getName(), resultList.get(0).getName()),
                () -> Assertions.assertEquals(expectedList.get(0).getImage(), resultList.get(0).getImage()),
                () -> Assertions.assertEquals(expectedList.get(0).getImageString(), resultList.get(0).getImageString()),
                () -> Assertions.assertEquals(expectedList.get(0).getCreator().getPhoto(), resultList.get(0).getCreator().getPhoto()),
                () -> Assertions.assertEquals(expectedList.get(0).getCreator().getImage(), resultList.get(0).getCreator().getImage()));
    }

    @Test
    public void getByStatus_should_ReturnListOfAddons_when_AddonsExist() {
        Addon mockAddon = createMockAddon();
        Mockito.when(mockRepository.getByStatus(mockAddon.getStatus().getName())).thenReturn(List.of(mockAddon));

        List<Addon> resultList = addonService.getByStatus(mockAddon.getStatus().getName());

        List<Addon> expectedList = List.of(mockAddon);
        expectedList.forEach(this::setStaticMockAddonFields);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedList.size(), resultList.size()),
                () -> Assertions.assertEquals(expectedList.get(0).getId(), resultList.get(0).getId()),
                () -> Assertions.assertEquals(expectedList.get(0).getName(), resultList.get(0).getName()),
                () -> Assertions.assertEquals(expectedList.get(0).getImage(), resultList.get(0).getImage()),
                () -> Assertions.assertEquals(expectedList.get(0).getImageString(), resultList.get(0).getImageString()),
                () -> Assertions.assertEquals(expectedList.get(0).getBinaryContent(), resultList.get(0).getBinaryContent()));
    }

    @Test
    public void getTotalDownloads_Should_CallRepository() {
        addonService.getTotalDownloads();
        Mockito.verify(mockRepository, Mockito.times(1)).getTotalDownloadCount();
    }

    @Test
    public void getPendingAddonsByName_Should_ReturnListOfAddon_When_AddonsExist() {
        Addon mockAddon = createMockAddon();
        List<Addon> expectedList = List.of(mockAddon);
        expectedList.forEach(this::setStaticMockAddonFields);

        Mockito.when(mockRepository.getPendingAddonsByName(mockAddon.getName())).thenReturn(List.of(mockAddon));

        List<Addon> resultList = addonService.getPendingAddonsByName(mockAddon.getName());

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedList.size(), resultList.size()),
                () -> Assertions.assertEquals(expectedList.get(0).getId(), resultList.get(0).getId()),
                () -> Assertions.assertEquals(expectedList.get(0).getImage(), resultList.get(0).getImage()),
                () -> Assertions.assertEquals(expectedList.get(0).getImageString(), resultList.get(0).getImageString()));
    }

    private void setStaticMockAddonFields(Addon mockAddon) {
        setAddonImageString(mockAddon);
        setAddonCreatorImageString(mockAddon);
        setAddonTotalScore(mockAddon);
    }
}
