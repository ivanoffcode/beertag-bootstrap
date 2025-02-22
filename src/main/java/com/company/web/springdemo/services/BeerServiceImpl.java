package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.FilterOptions;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeerServiceImpl implements BeerService {

    private static final String MODIFY_BEER_ERROR_MESSAGE = "Only admin or beer creator can modify a beer.";

    private final BeerRepository repository;

    @Autowired
    public BeerServiceImpl(BeerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Beer> get(FilterOptions filterOptions) {
        return repository.get(filterOptions);
    }

    @Override
    public Beer get(int id) {
        return repository.get(id);
    }

    @Override
    public void create(Beer beer, User user) {
        boolean duplicateExists = true;
        try {
            repository.get(beer.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Beer", "name", beer.getName());
        }

        beer.setCreatedBy(user);
        repository.create(beer);
    }

    @Override
    public void update(Beer beer, User user) {
        checkModifyPermissions(beer.getId(), user);

        boolean duplicateExists = true;
        try {
            Beer existingBeer = repository.get(beer.getName());
            if (existingBeer.getId() == beer.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Beer", "name", beer.getName());
        }

        repository.update(beer);
    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(id, user);
        repository.delete(id);
    }

    private void checkModifyPermissions(int beerId, User user) {
        Beer beer = repository.get(beerId);
        if (!(user.isAdmin() || beer.getCreatedBy().equals(user))) {
            throw new AuthorizationException(MODIFY_BEER_ERROR_MESSAGE);
        }
    }

}
