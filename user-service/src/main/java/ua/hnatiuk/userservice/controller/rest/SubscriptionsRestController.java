package ua.hnatiuk.userservice.controller.rest;

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
public class SubscriptionsRestController {
    private final SubscriptionsService subscriptionsService;
    private final SubscriptionMapper mapper;
    @Value("${carnotificator.inner-api-key}")
    private String innerApiKey;
    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptions(
            @RequestParam(name = "inner_key") String innerKey,
            @RequestParam(name = "active", required = false, defaultValue = "false") Boolean onlyActive
    ) {
        if (!innerKey.equals(innerApiKey)) {
            log.warn("Accepted request with invalid inner key");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<SubscriptionDTO> subscriptionDTOList
                = subscriptionsService.findAll(onlyActive).stream()
                .map(mapper::subscriptionToDTO)
                .toList();

        log.debug("Responsing with all active subscriptions");
        return ResponseEntity.ok(subscriptionDTOList);
    }
}
