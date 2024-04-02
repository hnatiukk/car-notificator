package ua.hnatiuk.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Controller
@RequestMapping("/account")
@Slf4j
public class AccountController {
    @GetMapping
    public String getAccountPage() {
        log.debug("Returning account page");
        return "account/settings";
    }
}
