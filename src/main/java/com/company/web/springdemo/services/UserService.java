package com.company.web.springdemo.services;

import com.company.web.springdemo.models.User;

import java.util.List;

public interface UserService {

    List<User> get();

    User get(int id);

    User get(String username);

    void create(User user);

    void addBeerToWishList(int userId, int beerId);

    void removeFromWishList(int userId, int beerId);

}
