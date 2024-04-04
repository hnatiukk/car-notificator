package ua.hnatiuk.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.hnatiuk.dto.MessageDTO;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "NotificationController", description = "Controller for interactions with notification service")
public class NotificationController {
    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;
    @PostMapping("/send")
    @Operation(summary = "Send message to user")
    @ApiResponse(responseCode = "200", description = "Message was successfully sent")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO) {
        log.debug("Received attempt to send message to chat id {}", messageDTO.getChatId());
        kafkaTemplate.send("carnotificator.telegram", messageDTO);
        log.debug("Sent message to Kafka");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
