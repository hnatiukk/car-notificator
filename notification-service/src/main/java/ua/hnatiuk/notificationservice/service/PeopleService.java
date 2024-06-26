package ua.hnatiuk.notificationservice.service;

import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.hnatiuk.dto.PersonDTO;
import ua.hnatiuk.notificationservice.exception.EmailNotFoundException;
import ua.hnatiuk.notificationservice.feign.UserServiceClient;


/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PeopleService {
    private final UserServiceClient userServiceClient;

    /**
     * Sends request to assign chat id
     * @param email Email of person to assign
     * @param chatId Chat id to assign
     */
    public void assignChatId(String email, Long chatId){
        try (Response response = userServiceClient.assignTgChatId(new PersonDTO(email, chatId))){
            if (response.status() == 404) {
                throw new EmailNotFoundException();
            }
        }

        log.info("Assigned chat id {} to {}", chatId, email);
    }
}
