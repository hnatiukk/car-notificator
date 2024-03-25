package ua.hnatiuk.telegramnotificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.hnatiuk.telegramnotificationservice.bot.TelegramNotificationBot;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramNotificationBot bot;
    @Override
    public void sendNotification(Long chatId, String message) {
        bot.sendMessage(chatId, message);
    }
}
