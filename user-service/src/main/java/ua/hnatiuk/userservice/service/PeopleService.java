package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import ua.hnatiuk.userservice.exception.EmailNotFoundException;
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
@Slf4j
public class PeopleService {
    private final PeopleRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Person register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        Person savedPerson = repository.save(person);
        log.info("Successfully registered {}", person.getEmail());
        return savedPerson;
    }

    public Optional<Person> findByEmail(String email) {
        return repository.findByEmail(email);
    }
    public Person findByEmailAndInitSubscriptions(String email) {
        Person person = repository.findByEmail(email).get();

        initSubscriptions(person);

        return person;
    }

    public void initSubscriptions(Person person) {
        Hibernate.initialize(person.getSubscriptions());
        log.debug("Initialized subscriptions for {}", person.getEmail());
    }
    @Transactional
    public void assignChatId(String email, Long chatId) {
        Optional<Person> personOptional = findByEmail(email);

        if (personOptional.isEmpty()) {
            throw new EmailNotFoundException();
        }

        Person person = personOptional.get();
        person.setTgChatId(chatId);
    }
}
