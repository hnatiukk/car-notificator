package ua.hnatiuk.notificationservice.listener;

import ua.hnatiuk.notificationservice.model.dto.MessageDTO;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
public interface NotificationListener {

    void sendNotificationWithPhoto(MessageDTO messageDTO);
}
