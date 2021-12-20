package com.company.addonis.repositories.contracts;

import com.company.addonis.models.User;

import java.util.List;


public interface UserRepository extends BaseModifyRepository<User> {

    List<User> search(String searchedValue);

    List<User> getAllUsers();
}
