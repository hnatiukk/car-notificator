package ua.hnatiuk.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * @author Hnatiuk Volodymyr on 22.03.2024.
 */
@Controller
@RequestMapping("/subscriptions")
public class SubscriptionsController {
    @GetMapping
    public String home(Principal principal, Model model) {
        model.addAttribute("email", principal.getName());

        return "home/subscriptions";
    }
}
