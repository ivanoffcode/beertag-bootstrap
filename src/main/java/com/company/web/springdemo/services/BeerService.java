package com.company.web.springdemo.services;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.FilterOptions;
import com.company.web.springdemo.models.User;

import java.util.List;

public interface BeerService {

    List<Beer> get(FilterOptions filterOptions);

    Beer get(int id);

    void create(Beer beer, User user);

    void update(Beer beer, User user);

    void delete(int id, User user);

}
