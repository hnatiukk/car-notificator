package ua.hnatiuk.userservice.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.hnatiuk.dto.SubscriptionDTO;
import ua.hnatiuk.userservice.model.mapper.SubscriptionMapper;
import ua.hnatiuk.userservice.service.SubscriptionsService;

import java.util.List;

/**
 * @author Hnatiuk Volodymyr on 04.04.2024.
 */
@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SubscriptionsRestController", description = "Controller for interactions with subscriptions")
public class SubscriptionsRestController {
    private final SubscriptionsService subscriptionsService;
    private final SubscriptionMapper mapper;
    @Value("${carnotificator.inner-api-key}")
    private String innerApiKey;
    @GetMapping
    @Operation(summary = "Get subscriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Invalid inner api key", content = @Content),
            @ApiResponse(responseCode = "200", description = "Returns subscriptions array")
    })
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptions(
            @RequestParam(name = "inner_key") String innerKey,
            @RequestParam(name = "active", required = false, defaultValue = "false") Boolean onlyActive
    ) {
        if (!innerKey.equals(innerApiKey)) {
            log.warn("Accepted request with invalid inner key");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<SubscriptionDTO> subscriptionDTOList
                = subscriptionsService.findAllByIsActive(onlyActive).stream()
                .map(mapper::subscriptionToDTO)
                .toList();

        log.debug("Responsing with all active subscriptions");
        return ResponseEntity.ok(subscriptionDTOList);
    }
}
