package ua.hnatiuk.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    @GetMapping
    public String getAccountPage() {
        return "account/settings";
    }
}
