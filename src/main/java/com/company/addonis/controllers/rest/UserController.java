package com.company.addonis.controllers.rest;

import com.company.addonis.controllers.AuthenticationHelper;
import com.company.addonis.exceptions.DuplicateEntityException;
import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.exceptions.UnauthorizedOperationException;
import com.company.addonis.models.User;
import com.company.addonis.models.dtos.UserDto;
import com.company.addonis.models.mappers.UserMapper;
import com.company.addonis.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static com.company.addonis.utils.UserRoleValidator.throwIfNotAdmin;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserController(UserMapper userMapper, UserService userService, AuthenticationHelper authenticationHelper) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping()
    public List<User> getAll(@RequestHeader HttpHeaders headers) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            return userService.getAll();
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getById(@RequestHeader HttpHeaders headers, @Valid @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDto(userDto);
            userService.create(user);
            return user;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("{id}")
    public User update(@Valid @RequestBody UserDto userDto, @Valid @PathVariable int id) {
        try {
            User user = userMapper.fromDto(userDto, id);
            userService.update(user);
            return user;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public void delete(@RequestHeader HttpHeaders headers, @Valid @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            userService.delete(id);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @PutMapping("/{userId}/addPhoto")
    public void addUserPhoto(@RequestHeader HttpHeaders headers, @PathVariable int userId, MultipartFile image) throws IOException {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            User user = userService.getById(userId);
            if (!image.isEmpty()) {
                user.setPhoto(Base64.getEncoder().encode(image.getBytes()));
                userService.update(user);
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{userId}/block")
    public User blockUser(@RequestHeader HttpHeaders headers, @PathVariable int userId) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            User user = userService.getById(userId);
            user.setEnabled(false);

            userService.update(user);
            return user;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("/{userId}/unblock")
    public User unblockUser(@RequestHeader HttpHeaders headers, @PathVariable int userId) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            User user = userService.getById(userId);
            user.setEnabled(true);

            userService.update(user);
            return user;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<User> search(@RequestHeader HttpHeaders headers, String searchedValue) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            return userService.search(searchedValue);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}