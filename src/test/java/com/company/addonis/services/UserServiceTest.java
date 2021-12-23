package com.company.addonis.services;

import com.company.addonis.exceptions.DuplicateEntityException;
import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.models.User;
import com.company.addonis.repositories.contracts.AddonRepository;
import com.company.addonis.repositories.contracts.UserRepository;
import com.company.addonis.services.contracts.EmailSenderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.company.addonis.Helpers.createMockUser;
import static com.company.addonis.utils.ObjectFieldsSetter.setUserImageString;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository mockRepository;

    @Mock
    AddonRepository addonRepository;

    @Mock
    EmailSenderService emailSenderService;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void getAll_Should_CallRepository() {
        Mockito.when(mockRepository.getAll()).thenReturn(List.of(createMockUser()));
        userService.getAll();

        Mockito.verify(mockRepository, Mockito.times(1)).getAll();
    }

    @Test
    public void getById_Should_ReturnUser_When_UserExist() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.getById(mockUser.getId())).thenReturn(mockUser);
        Mockito.when(addonRepository.getAddonsByUser(mockUser.getId())).thenReturn(new ArrayList<>());
        setUserImageString(mockUser);
        mockUser.setUploadedAddons(0);

        User resultUser = userService.getById(mockUser.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(mockUser.getId(), resultUser.getId()),
                () -> Assertions.assertEquals(mockUser.getUsername(), resultUser.getUsername()),
                () -> Assertions.assertEquals(mockUser.getEmail(), resultUser.getEmail()),
                () -> Assertions.assertEquals(mockUser.getPhoneNumber(), resultUser.getPhoneNumber()),
                () -> Assertions.assertEquals(mockUser.getPhoto(), resultUser.getPhoto()),
                () -> Assertions.assertEquals(mockUser.getImage(), resultUser.getImage()));
    }

    @Test
    public void getByField_Should_ReturnUser_When_UserExists() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.getByField("username", mockUser.getUsername())).thenReturn(mockUser);

        User resultUser = userService.getByField("username", mockUser.getUsername());
        setUserImageString(mockUser);

        Assertions.assertAll(
                () -> Assertions.assertEquals(mockUser.getId(), resultUser.getId()),
                () -> Assertions.assertEquals(mockUser.getUsername(), resultUser.getUsername()),
                () -> Assertions.assertEquals(mockUser.getPhoto(), resultUser.getPhoto()),
                () -> Assertions.assertEquals(mockUser.getImage(), resultUser.getImage()));
    }

    @Test
    public void update_Should_CallRepository_When_UpdatingSameUser() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.getByField("email", mockUser.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByField("username", mockUser.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByField("phoneNumber", mockUser.getPhoneNumber()))
                .thenThrow(EntityNotFoundException.class);

        userService.update(mockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockUser);
    }

    @Test
    public void update_Should_CallRepository_When_UpdatingSameUserWithValidParams() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.getByField("email", mockUser.getEmail()))
                .thenReturn(mockUser);
        Mockito.when(mockRepository.getByField("username", mockUser.getUsername()))
                .thenReturn(mockUser);
        Mockito.when(mockRepository.getByField("phoneNumber", mockUser.getPhoneNumber()))
                .thenReturn(mockUser);

        userService.update(mockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockUser);
    }

    @Test
    public void update_Should_Throw_When_UsernameIsDuplicated() {
        User mockUser = createMockUser();
        User mockUserSecond = createMockUser();
        mockUserSecond.setId(111);

        Mockito.when(mockRepository.getByField("username", mockUser.getUsername()))
                .thenReturn(mockUserSecond);

        Assertions.assertThrows(DuplicateEntityException.class, () -> userService.update(mockUser));
    }

    @Test
    public void update_Should_Throw_When_EmailIsDuplicated() {
        User mockUser = createMockUser();
        User mockUserSecond = createMockUser();
        mockUserSecond.setId(111);

        Mockito.when(mockRepository.getByField("username", mockUser.getUsername()))
                .thenReturn(mockUser);
        Mockito.when(mockRepository.getByField("email", mockUser.getEmail()))
                .thenReturn(mockUserSecond);

        Assertions.assertThrows(DuplicateEntityException.class, () -> userService.update(mockUser));
    }

    @Test
    public void update_Should_Throw_When_PhoneNumberIsDuplicated() {
        User mockUser = createMockUser();
        User mockUserSecond = createMockUser();
        mockUserSecond.setId(111);

        Mockito.when(mockRepository.getByField("username", mockUser.getUsername()))
                .thenReturn(mockUser);
        Mockito.when(mockRepository.getByField("email", mockUser.getEmail()))
                .thenReturn(mockUser);
        Mockito.when(mockRepository.getByField("phoneNumber", mockUser.getPhoneNumber()))
                .thenReturn(mockUserSecond);

        Assertions.assertThrows(DuplicateEntityException.class, () -> userService.update(mockUser));
    }

    @Test
    public void delete_Should_CallRepository() {
        userService.delete(createMockUser().getId());
        Mockito.verify(mockRepository, Mockito.times(1)).delete(createMockUser().getId());
    }

    @Test
    public void search_Should_CallRepository() {
        userService.search(createMockUser().getUsername());
        Mockito.verify(mockRepository, Mockito.times(1)).search(createMockUser().getUsername());
    }

    @Test
    public void getAllUsers_Should_CallRepository() {
        userService.getAllUsers();
        Mockito.verify(mockRepository, Mockito.times(1)).getAllUsers();
    }

    @Test
    public void create_Should_CallUserRepositoriesAndEmailSenderService(){
        User user = createMockUser();

        Mockito.when(mockRepository.getByField("username", user.getUsername())).thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByField("email", user.getEmail())).thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByField("phoneNumber", user.getPhoneNumber())).thenThrow(EntityNotFoundException.class);

        Mockito.doNothing().when(mockRepository).create(user);
        Mockito.doNothing().when(emailSenderService).sendEmail(user);

        userService.create(user);

        Mockito.verify(mockRepository, Mockito.times(1)).create(user);
        Mockito.verify(emailSenderService, Mockito.times(1)).sendEmail(user);
    }
}
