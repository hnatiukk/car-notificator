package ua.hnatiuk.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.hnatiuk.notificationservice.model.dto.MessageDTO;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;
    @PostMapping("/send")
    public ResponseEntity<HttpStatus> sendMessage(@RequestBody MessageDTO messageDTO) {
        kafkaTemplate.send("carnotificator.telegram", messageDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
