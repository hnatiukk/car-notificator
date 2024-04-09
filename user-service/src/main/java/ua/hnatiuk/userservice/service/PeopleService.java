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

    /**
     * Registers new person
     * @param person Person entity to register
     * @return Saved person entity
     */
    @Transactional
    public Person register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        Person savedPerson = repository.save(person);
        log.info("Successfully registered {}", person.getEmail());
        return savedPerson;
    }

    /**
     * Finds person by email
     * @param email Email of person to find
     * @return Optional of found person
     */
    public Optional<Person> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    /**
     * Finds person by email and initialize his subscriptions
     * @param email Email of person to find
     * @return Found person entity
     */
    public Person findByEmailAndInitSubscriptions(String email) {
        Person person = findByEmail(email).get();

        initSubscriptions(person);

        return person;
    }

    /**
     * Initializes subscriptions for person
     * @param person Person to initialize his subscriptions
     */
    public void initSubscriptions(Person person) {
        Hibernate.initialize(person.getSubscriptions());
        log.debug("Initialized subscriptions for {}", person.getEmail());
    }

    /**
     * Assigns chat id to person by email
     * @param email Email of person
     * @param chatId Chat id to assign
     * @return Updated person
     */
    @Transactional
    public Person assignChatId(String email, Long chatId) {
        Optional<Person> personOptional = findByEmail(email);

        if (personOptional.isEmpty()) {
            log.debug("Could not find email {}", email);
            throw new EmailNotFoundException();
        }

        Person person = personOptional.get();
        person.setTgChatId(chatId);
        log.info("Successfully assigned {} chat id to {}", chatId, email);
        return person;
    }
}
