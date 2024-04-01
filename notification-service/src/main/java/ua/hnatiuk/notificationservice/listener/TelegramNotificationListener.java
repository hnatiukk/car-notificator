package ua.hnatiuk.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ua.hnatiuk.notificationservice.bot.TelegramNotificationBot;
import ua.hnatiuk.notificationservice.model.dto.MessageDTO;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Component
@RequiredArgsConstructor
public class TelegramNotificationListener implements NotificationListener {
    private final TelegramNotificationBot bot;
    @Override
    @KafkaListener(topics = "carnotificator.telegram", groupId = "main", containerFactory = "factory")
    public void sendNotificationWithPhoto(MessageDTO messageDTO) {
        bot.sendMessageWithPhoto(messageDTO.getChatId(), messageDTO.getText(), messageDTO.getPhotoUrl());
    }
}
