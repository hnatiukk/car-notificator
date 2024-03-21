package org.hnatiuk.carnotificator.service;

import lombok.RequiredArgsConstructor;
import org.hnatiuk.carnotificator.model.entity.Person;
import org.hnatiuk.carnotificator.repository.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final PersonRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        repository.save(person);
    }
}
