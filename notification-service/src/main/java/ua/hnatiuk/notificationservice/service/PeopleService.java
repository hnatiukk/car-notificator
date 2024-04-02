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
    public String tryAssignChatId(String userMessageText, Long chatId) {
        String response;

        if (userMessageText.startsWith("/link")) {
            String[] split = userMessageText.split(" ");
            if (split.length != 2) {
                response = "Введіть у форматі:\n\n/link \"email адреса, на яку реєструвались\"";
            } else {
                try {
                    String email = split[1];
                    if (Pattern.matches("^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", email)) {
                        assignChatId(email, chatId);
                        response = "Ви успішно прив`язали телеграм.";
                    } else response = "Це не email.";
                } catch (EmailNotFoundException e) {
                    log.warn("This email is not registered");
                    response = "Такий email не зареєстровано.";
                }
            }
        } else
            response = "Щоб з`єднати ваш телеграм з CarNotificator, відправте:\n\n/link \"email адреса, на яку реєструвались\"";
        return response;
    }
    @Transactional
    public void assignChatId(String email, Long chatId){
        Optional<Person> personOptional = repository.findByEmail(email);

        Person person = personOptional.orElseThrow(EmailNotFoundException::new);

        person.setTgChatId(chatId);
        log.info("Assigned chat id {} to {}", chatId, email);
    }
}
