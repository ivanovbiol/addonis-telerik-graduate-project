package com.company.addonis.services;

import com.company.addonis.exceptions.DuplicateEntityException;
import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.models.User;
import com.company.addonis.repositories.contracts.AddonRepository;
import com.company.addonis.repositories.contracts.UserRepository;
import com.company.addonis.services.contracts.EmailSenderService;
import com.company.addonis.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.company.addonis.utils.ObjectFieldsSetter.setUserImageString;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AddonRepository addonRepository;
    private final EmailSenderService emailSenderService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AddonRepository addonRepository,
                           EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.addonRepository = addonRepository;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int userId) {
        User user = userRepository.getById(userId);
        setUserImageString(user);
        user.setUploadedAddons(addonRepository.getAddonsByUser(user.getId()).size());
        return user;
    }

    @Override
    public User getByField(String fieldName, String value) {
        User user = userRepository.getByField(fieldName, value);
        setUserImageString(user);
        return user;
    }

    @Override
    public void update(User user) {
        validateUserInfo(user);
        userRepository.update(user);
    }

    @Override
    public void create(User user) {
        validateUserInfo(user);
        userRepository.create(user);
        emailSenderService.sendEmail(user);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }

    @Override
    public List<User> search(String searchedValue) {
        List<User> users = userRepository.search(searchedValue);
        setUserImageString(users);
        users.forEach(user -> user.setUploadedAddons(addonRepository.getAddonsByUser(user.getId()).size()));
        return users;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        setUserImageString(users);
        users.forEach(user -> user.setUploadedAddons(addonRepository.getAddonsByUser(user.getId()).size()));
        return users;
    }

    public void validateUserInfo(User user) {
        validateUsername(user);
        validateEmail(user);
        validatePhoneNumber(user);
    }

    public void validateEmail(User user) {
        boolean duplicateExists = true;
        try {
            User userExist = userRepository.getByField("email", user.getEmail());
            if (userExist.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
    }

    public void validateUsername(User user) {
        boolean duplicateExists = true;
        try {
            User userExists = userRepository.getByField("username", user.getUsername());
            if (userExists.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }
    }

    public void validatePhoneNumber(User user) {
        boolean duplicateExists = true;
        try {
            User userExist = userRepository.getByField("phoneNumber", user.getPhoneNumber());
            if (userExist.getId() == user.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "phone number", user.getPhoneNumber());
        }
    }

}

