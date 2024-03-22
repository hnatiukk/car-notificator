package ua.hnatiuk.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.service.AuthService;
import org.springframework.web.bind.annotation.*;


/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    @GetMapping("/signup")
    public String getSignUpPage(@ModelAttribute("person") Person person) {
        return "auth/signup";
    }
    @PostMapping("/signup")
    public String signUp(@ModelAttribute("person") @Valid Person person) {

        service.register(person);

        return "redirect:/login";
    }
}
