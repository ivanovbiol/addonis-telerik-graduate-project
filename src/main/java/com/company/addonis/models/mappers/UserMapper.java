package com.company.addonis.models.mappers;

import com.company.addonis.models.User;
import com.company.addonis.models.dtos.RegisterDto;
import com.company.addonis.models.dtos.UserDetailsUpdateDto;
import com.company.addonis.models.dtos.UserDto;
import com.company.addonis.services.contracts.UserService;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class UserMapper {

    private final UserService userService;

    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDto(UserDto userDto, int id) {
        User user = userService.getById(id);
        dtoToObject(userDto, user);
        return user;
    }

    private void dtoToObject(UserDto userDto, User user) {
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());

    }

    public UserDto fromDtoToUser(RegisterDto registerDto) {
        UserDto userDto = new UserDto();
        userDto.setUsername(registerDto.getUsername());
        userDto.setEmail(registerDto.getEmail());
        userDto.setPassword(registerDto.getPassword());
        userDto.setPhoneNumber(registerDto.getPhoneNumber());
        return userDto;
    }


    public User updateUserDetails(UserDetailsUpdateDto userDetailsUpdateDto, int id) throws IOException {
        User user = userService.getById(id);
        if (!userDetailsUpdateDto.getEmail().isEmpty()) {
            user.setEmail(userDetailsUpdateDto.getEmail());
        }
        if (!userDetailsUpdateDto.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(userDetailsUpdateDto.getPhoneNumber());
        }
        if (!userDetailsUpdateDto.getPhoto().isEmpty()) {
            user.setPhoto(userDetailsUpdateDto.getPhoto().getBytes());
        }
        return user;
    }
}