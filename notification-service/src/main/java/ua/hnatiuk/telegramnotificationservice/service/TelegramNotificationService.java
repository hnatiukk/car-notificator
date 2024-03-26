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
    public void sendNotificationWithPhoto(Long chatId, String message, String photoUrl) {
        bot.sendMessageWithPhoto(chatId, message, photoUrl);
    }
}
