package ua.hnatiuk.telegramnotificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.hnatiuk.telegramnotificationservice.service.NotificationService;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @PostMapping("/send")
    public ResponseEntity<HttpStatus> sendMessage(@RequestParam("chat_id") Long chatId,
                                                  @RequestBody String message,
                                                  @RequestParam("photo_url") String photoUrl) {
        notificationService.sendNotificationWithPhoto(chatId, message, photoUrl);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
