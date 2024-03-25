package ua.hnatiuk.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.service.PeopleService;
import org.springframework.web.bind.annotation.*;
import ua.hnatiuk.userservice.util.PersonValidator;


/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {
    private final PeopleService service;
    private final PersonValidator validator;

    @GetMapping("/login")
    public String getLoginPage(@ModelAttribute("person") Person person) {
        return "auth/login";
    }
    @GetMapping("/signup")
    public String getSignupPage(@ModelAttribute("person") Person person) {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        validator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        service.register(person);

        return "redirect:/login";
    }
}
