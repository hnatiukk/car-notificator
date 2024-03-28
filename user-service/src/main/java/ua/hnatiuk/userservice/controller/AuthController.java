package ua.hnatiuk.userservice.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AuthController {
    private final PeopleService service;
    private final PersonValidator validator;
    private final MailService mailService;

    @GetMapping("/login")
    public String getLoginPage(@ModelAttribute("person") Person person) {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String getSignupPage(@ModelAttribute("person") Person person) {
        return "auth/signup";
    }

    @PostMapping("/confirm-email")
    public String getConfirmEmailPage(@ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult,
                                      HttpSession httpSession) {
        validator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        int code = mailService.sendVerificationCode(person.getEmail());
        httpSession.setAttribute("verificationCode", code);
        httpSession.setAttribute("person", person);

        return "auth/confirm-email";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("person") Person person,
                         @RequestParam("verification_code") Integer verificationCode,
                         HttpSession httpSession) {
        if (!verificationCode.equals(httpSession.getAttribute("verificationCode"))) {
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
