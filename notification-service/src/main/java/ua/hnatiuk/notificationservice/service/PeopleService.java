package ua.hnatiuk.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnatiuk.notificationservice.exception.EmailNotFoundException;
import ua.hnatiuk.notificationservice.model.entity.Person;
import ua.hnatiuk.notificationservice.repository.PeopleRepository;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PeopleService {
    private final PeopleRepository repository;
    @Transactional
    public void assignChatId(String email, Long chatId){
        Optional<Person> personOptional = repository.findByEmail(email);

        Person person = personOptional.orElseThrow(EmailNotFoundException::new);

        person.setTgChatId(chatId);
        log.info("Assigned chat id {} to {}", chatId, email);
    }
}
