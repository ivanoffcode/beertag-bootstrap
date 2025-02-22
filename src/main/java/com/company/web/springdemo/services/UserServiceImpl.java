package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.BeerRepository;
import com.company.web.springdemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BeerRepository beerRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, BeerRepository beerRepository) {
        this.repository = repository;
        this.beerRepository = beerRepository;
    }

    @Override
    public List<User> get() {
        return repository.get();
    }

    @Override
    public User get(int id) {
        return repository.get(id);
    }

    @Override
    public User get(String username) {
        return repository.get(username);
    }

    @Override
    public void create(User user) {
        boolean duplicateExists = true;
        try {
            repository.get(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }

        repository.create(user);
    }

    @Override
    public void addBeerToWishList(int userId, int beerId) {
        User user = repository.get(userId);
        if (user.getWishList().stream().anyMatch(b -> b.getId() == beerId)) {
            return;
        }
        Beer beer = beerRepository.get(beerId);
        user.getWishList().add(beer);
        repository.update(user);
    }

    @Override
    public void removeFromWishList(int userId, int beerId) {
        User user = repository.get(userId);
        if (user.getWishList().stream().noneMatch(b -> b.getId() == beerId)) {
            throw new EntityNotFoundException("Beer", beerId);
        }
        user.getWishList().removeIf(b -> b.getId() == beerId);
        repository.update(user);
    }
}
