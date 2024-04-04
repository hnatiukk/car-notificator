package ua.hnatiuk.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ua.hnatiuk.notificationservice.bot.TelegramNotificationBot;
import ua.hnatiuk.dto.MessageDTO;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramNotificationListener implements NotificationListener {
    private final TelegramNotificationBot bot;
    @Override
    @KafkaListener(topics = "carnotificator.telegram", groupId = "main", containerFactory = "factory")
    public void sendNotificationWithPhoto(MessageDTO messageDTO) {
        log.debug("Received message from Kafka");
        bot.sendMessageWithPhoto(messageDTO);
    }
}
