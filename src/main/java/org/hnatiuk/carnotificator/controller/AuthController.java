package org.hnatiuk.carnotificator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hnatiuk.carnotificator.exception.InvalidCredentialsException;
import org.hnatiuk.carnotificator.model.dto.PersonDTO;
import org.hnatiuk.carnotificator.model.entity.Person;
import org.hnatiuk.carnotificator.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid PersonDTO personDTO,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder builder = new StringBuilder();
            bindingResult.getFieldErrors()
                    .forEach(error -> builder.append(error.getDefaultMessage()).append(". "));
            throw new InvalidCredentialsException(builder.toString());
        }

        service.authenticateByDTO(personDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid PersonDTO personDTO,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder builder = new StringBuilder();
            bindingResult.getFieldErrors()
                    .forEach(error -> builder.append(error).append(". "));
            throw new InvalidCredentialsException(builder.toString());
        }

        Person person = service.personDTOToPerson(personDTO);
        service.register(person);

        return ResponseEntity.ok().build();
    }
}
