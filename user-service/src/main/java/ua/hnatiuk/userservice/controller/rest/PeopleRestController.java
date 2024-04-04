package ua.hnatiuk.userservice.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hnatiuk.dto.PersonDTO;
import ua.hnatiuk.userservice.exception.EmailNotFoundException;
import ua.hnatiuk.userservice.service.PeopleService;

/**
 * @author Hnatiuk Volodymyr on 04.04.2024.
 */
@RestController
@RequestMapping("/api/v1/people")
@RequiredArgsConstructor
public class PeopleRestController {
    private final PeopleService peopleService;
    @Value("${carnotificator.inner-api-key}")
    private String innerApiKey;
    @PostMapping("/assign-tg-chat-id")
    public ResponseEntity<Void> assignTgChatId(
            @RequestParam(name = "inner_key") String innerKey,
            @RequestBody PersonDTO personDTO
            ) {
        if (!innerKey.equals(innerApiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            peopleService.assignChatId(personDTO.getEmail(), personDTO.getTgChatId());
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }
}
