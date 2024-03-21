package org.hnatiuk.carnotificator.controller;

import lombok.RequiredArgsConstructor;
import org.hnatiuk.carnotificator.model.entity.Person;
import org.hnatiuk.carnotificator.security.PersonDetails;
import org.hnatiuk.carnotificator.service.AuthService;
import org.hnatiuk.carnotificator.service.PersonDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


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
