package com.company.web.springdemo.controllers.rest;

import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.helpers.AuthenticationHelper;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    public static final String ERROR_MESSAGE = "You are not authorized to browse user information.";
    private final UserService service;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserRestController(UserService service, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> get(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
            }
            return service.get();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User get(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            return service.get(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}/wish-list")
    public List<Beer> getWishList(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            return new ArrayList<>(service.get(id).getWishList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/wish-list/{beerId}")
    public void addToWishList(
            @RequestHeader HttpHeaders headers,
            @PathVariable int id,
            @PathVariable int beerId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            service.addBeerToWishList(id, beerId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/wish-list/{beerId}")
    public void removeFromWishList(
            @RequestHeader HttpHeaders headers,
            @PathVariable int id,
            @PathVariable int beerId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            service.removeFromWishList(id, beerId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private static void checkAccessPermissions(int targetUserId, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != targetUserId) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

}
