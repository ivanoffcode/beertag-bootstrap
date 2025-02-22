package com.company.web.springdemo.repositories;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.FilterOptions;

import java.util.List;

public interface BeerRepository {

    List<Beer> get(FilterOptions filterOptions);

    Beer get(int id);

    Beer get(String name);

    void create(Beer beer);

    void update(Beer beer);

    void delete(int id);

}
