package ua.hnatiuk.observerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.hnatiuk.dto.MessageDTO;
import ua.hnatiuk.observerservice.feign.NotificationServiceClient;

/**
 * @author Hnatiuk Volodymyr on 06.04.2024.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationServiceClient client;

    public void sendNotification(MessageDTO messageDTO) {
        client.sendNotification(messageDTO);
        log.debug("Sent notification to chat id {}", messageDTO.getChatId());
    }
}
