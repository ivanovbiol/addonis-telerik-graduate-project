package com.company.addonis.services.contracts;


import com.company.addonis.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int userId);

    User getByField(String fieldName, String value);

    void create(User user);

    void update(User user);

    void delete(int id);

    List<User> search(String searchedValue);

    List<User> getAllUsers();

}
