package ua.hnatiuk.telegramnotificationservice.service;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
public interface NotificationService {

    void sendNotification(Long chatId, String message);
}
