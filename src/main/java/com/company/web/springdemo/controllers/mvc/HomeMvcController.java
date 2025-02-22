package com.company.web.springdemo.controllers.mvc;

import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.helpers.AuthenticationHelper;
import com.company.web.springdemo.models.FilterOptions;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.BeerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final BeerService beerService;

    @Autowired
    public HomeMvcController(AuthenticationHelper authenticationHelper, BeerService beerService) {
        this.authenticationHelper = authenticationHelper;
        this.beerService = beerService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("beers", beerService.get(new FilterOptions()));
        return "HomeView";
    }

    @GetMapping("/admin")
    public String showAdminPortal(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            if(user.isAdmin()){
                return "AdminPortalView";
            }
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
