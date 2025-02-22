package com.company.web.springdemo.controllers.mvc;

import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.helpers.AuthenticationHelper;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.BeerService;
import com.company.web.springdemo.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final BeerService beerService;

    public UserMvcController(AuthenticationHelper authenticationHelper, UserService userService, BeerService beerService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.beerService = beerService;
    }

    @PostMapping("/wishlist/{beerId}")
    public String updateWishList(@PathVariable int beerId, Model model, HttpSession session){
        try {
            User user;
            try {
                user = authenticationHelper.tryGetCurrentUser(session);
            } catch (AuthorizationException e) {
                return "redirect:/auth/login";
            }

            Beer beer = beerService.get(beerId);
            Set<Beer> wishlist = user.getWishList();
            if(wishlist.contains(beer)){
                userService.removeFromWishList(user.getId(), beerId);
            } else {
                userService.addBeerToWishList(user.getId(), beerId);
            }
            return "redirect:/beers";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}
