package ua.hnatiuk.userservice.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.service.MailService;
import ua.hnatiuk.userservice.service.PeopleService;
import org.springframework.web.bind.annotation.*;
import ua.hnatiuk.userservice.util.PersonValidator;


/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final PeopleService service;
    private final PersonValidator validator;
    private final MailService mailService;

    @GetMapping("/login")
    public String getLoginPage(@ModelAttribute("person") Person person) {
        log.debug("Returning login page");
        return "auth/login";
    }

    @GetMapping("/signup")
    public String getSignupPage(@ModelAttribute("person") Person person) {
        log.debug("Returning signup page");
        return "auth/signup";
    }

    @PostMapping("/confirm-email")
    public String getConfirmEmailPage(@ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult,
                                      HttpSession httpSession) {
        log.debug("Received request to confirm email");

        validator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            log.warn("Could not validate person with email: {}", person.getEmail());
            return "auth/signup";
        }

        int code = mailService.sendVerificationCode(person.getEmail());
        httpSession.setAttribute("verificationCode", code);
        httpSession.setAttribute("person", person);

        return "auth/confirm-email";
    }

    @PostMapping("/signup")
    public String signUp(@RequestParam("verification_code") Integer verificationCode,
                         HttpSession httpSession) {
        log.debug("Received request to sign up");

        if (!verificationCode.equals(httpSession.getAttribute("verificationCode"))) {
            log.warn("Invalid verification code. Expected {}, provided {}", httpSession.getAttribute("verificationCode"), verificationCode);
            httpSession.removeAttribute("person");
            httpSession.removeAttribute("verificationCode");
            return "redirect:/signup?error";
        }

        httpSession.removeAttribute("verificationCode");

        service.register((Person) httpSession.getAttribute("person"));
        httpSession.removeAttribute("person");

        return "redirect:/login";
    }
}
