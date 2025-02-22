package com.company.web.springdemo.controllers.mvc;

import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.helpers.AuthenticationHelper;
import com.company.web.springdemo.helpers.BeerMapper;
import com.company.web.springdemo.models.*;
import com.company.web.springdemo.services.BeerService;
import com.company.web.springdemo.services.StyleService;
import com.company.web.springdemo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/beers")
public class BeerMvcController {

    private final BeerService beerService;

    private final UserService userService;
    private final StyleService styleService;
    private final BeerMapper beerMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public BeerMvcController(BeerService beerService,
                             UserService userService, BeerMapper beerMapper,
                             StyleService styleService,
                             AuthenticationHelper authenticationHelper) {
        this.beerService = beerService;
        this.userService = userService;
        this.beerMapper = beerMapper;
        this.styleService = styleService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("styles")
    public List<Style> populateStyles() {
        return styleService.get();
    }

    @GetMapping
    public String showAllBeers(@ModelAttribute("filterOptions") FilterDto filterDto, Model model, HttpSession session) {
        FilterOptions filterOptions = new FilterOptions(
                filterDto.getName(),
                filterDto.getMinAbv(),
                filterDto.getMaxAbv(),
                filterDto.getStyleId(),
                filterDto.getSortBy(),
                filterDto.getSortOrder());
        List<Beer> beers = beerService.get(filterOptions);
        if (populateIsAuthenticated(session)){
            String currentUsername = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.get(currentUsername));
        }
        model.addAttribute("filterOptions", filterDto);
        model.addAttribute("beers", beers);
        return "BeersView";
    }

    @GetMapping("/{id}")
    public String showSingleBeer(@PathVariable int id, Model model) {
        try {
            Beer beer = beerService.get(id);
            model.addAttribute("beer", beer);
            return "BeerView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/new")
    public String showNewBeerPage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("beer", new BeerDto());
        return "BeerCreateView";
    }

    @PostMapping("/new")
    public String createBeer(@Valid @ModelAttribute("beer") BeerDto beerDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "BeerCreateView";
        }

        try {
            Beer beer = beerMapper.fromDto(beerDto);
            beerService.create(beer, user);
            return "redirect:/beers";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_beer", e.getMessage());
            return "BeerCreateView";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditBeerPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Beer beer = beerService.get(id);
            BeerDto beerDto = beerMapper.toDto(beer);
            model.addAttribute("beerId", id);
            model.addAttribute("beer", beerDto);
            return "BeerUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateBeer(@PathVariable int id,
                             @Valid @ModelAttribute("beer") BeerDto dto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "BeerUpdateView";
        }

        try {
            Beer beer = beerMapper.fromDto(id, dto);
            beerService.update(beer, user);
            return "redirect:/beers";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_beer", e.getMessage());
            return "BeerUpdateView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteBeer(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            beerService.delete(id, user);
            return "redirect:/beers";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

}
