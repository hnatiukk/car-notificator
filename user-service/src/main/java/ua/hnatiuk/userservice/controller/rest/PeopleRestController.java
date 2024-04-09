package ua.hnatiuk.userservice.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Tag(name = "PeopleRestController", description = "Controller for interactions with users")
public class PeopleRestController {
    private final PeopleService peopleService;
    @Value("${carnotificator.inner-api-key}")
    private String innerApiKey;

    /**
     * Assigns chat id to person my email
     * @param innerKey Inner api key
     * @param personDTO PersonDTO with email and chat id
     * @return 200 OK, 400 Provided email is not registered, 403 Invalid inner api key
     */
    @PostMapping("/assign-tg-chat-id")
    @Operation(summary = "Assign chat id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat id was successfully assigned"),
            @ApiResponse(responseCode = "400", description = "Provided email is not registered"),
            @ApiResponse(responseCode = "403", description = "Invalid inner api key")
    })
    public ResponseEntity<Void> assignTgChatId(
            @RequestParam(name = "inner_key") String innerKey,
            @RequestBody PersonDTO personDTO
            ) {

        if (!innerKey.equals(innerApiKey)) {
            log.warn("Accepted request with invalid inner key");
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
