package ua.hnatiuk.telegramnotificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.hnatiuk.telegramnotificationservice.bot.NotificationBot;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationBot bot;

    public void notify(Long chatId, String message) {
        bot.sendMessage(chatId, message);
    }
}
