package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.repository.PeopleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        repository.save(person);
    }

    public Optional<Person> findByEmail(String email) {
        return repository.findByEmail(email);
    }
    public Person findByEmailAndInitSubscriptions(String email) {
        Person person = repository.findByEmail(email).get();

        Hibernate.initialize(person.getSubscriptions());

        return person;
    }

    public void initSubscriptions(Person person) {
        Hibernate.initialize(person.getSubscriptions());
    }
}
