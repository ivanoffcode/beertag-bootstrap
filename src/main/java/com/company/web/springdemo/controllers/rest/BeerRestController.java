package com.company.web.springdemo.controllers.rest;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.helpers.AuthenticationHelper;
import com.company.web.springdemo.helpers.BeerMapper;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.BeerDto;
import com.company.web.springdemo.models.FilterOptions;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerRestController {

    private final BeerService service;
    private final BeerMapper beerMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public BeerRestController(BeerService service, BeerMapper beerMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.beerMapper = beerMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Beer> get(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minAbv,
            @RequestParam(required = false) Double maxAbv,
            @RequestParam(required = false) Integer styleId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        FilterOptions filterOptions = new FilterOptions(name, minAbv, maxAbv, styleId, sortBy, sortOrder);
        return service.get(filterOptions);
    }

    @GetMapping("/{id}")
    public Beer get(@PathVariable int id) {
        try {
            return service.get(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Beer create(@RequestHeader HttpHeaders headers, @Valid @RequestBody BeerDto beerDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Beer beer = beerMapper.fromDto(beerDto);
            service.create(beer, user);
            return beer;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Beer update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody BeerDto beerDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Beer beer = beerMapper.fromDto(id, beerDto);
            service.update(beer, user);
            return beer;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
