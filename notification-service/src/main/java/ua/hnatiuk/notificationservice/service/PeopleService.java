package ua.hnatiuk.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnatiuk.notificationservice.exception.EmailNotFoundException;
import ua.hnatiuk.notificationservice.model.entity.Person;
import ua.hnatiuk.notificationservice.repository.PeopleRepository;

import java.util.Optional;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository repository;
    @Transactional
    public void assignChatId(String email, Long chatId){
        Optional<Person> personOptional = repository.findByEmail(email);

        Person person = personOptional.orElseThrow(EmailNotFoundException::new);

        person.setTgChatId(chatId);
    }
}
