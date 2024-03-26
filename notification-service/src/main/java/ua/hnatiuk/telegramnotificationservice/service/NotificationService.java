package ua.hnatiuk.telegramnotificationservice.service;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
public interface NotificationService {

    void sendNotificationWithPhoto(Long chatId, String message, String photoUrl);
}
