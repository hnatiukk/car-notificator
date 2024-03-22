package ua.hnatiuk.userservice.controller;

import lombok.RequiredArgsConstructor;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestParam("email") String email,
                                       @RequestParam("password") String password) {
        Person person = new Person(email, password);

        service.register(person);

        return ResponseEntity.ok().build();
    }
}
