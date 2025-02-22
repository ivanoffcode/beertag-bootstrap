package com.company.web.springdemo.repositories;

import com.company.web.springdemo.models.User;

import java.util.List;

public interface UserRepository {

    List<User> get();

    User get(int id);

    User get(String username);

    void create(User user);

    void update(User user);

}
