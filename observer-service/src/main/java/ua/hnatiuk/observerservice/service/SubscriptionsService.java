package ua.hnatiuk.observerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.hnatiuk.dto.SubscriptionDTO;
import ua.hnatiuk.observerservice.feign.UserServiceClient;

import java.util.List;

/**
 * @author Hnatiuk Volodymyr on 26.03.2024.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionsService {
    private final UserServiceClient userServiceClient;

    /**
     * Returns all active subscriptions
     * @return List of active subscriptionDTOs
     */
    public List<SubscriptionDTO> getActiveSubscriptions() {
        log.debug("Getting all active subscriptions");
        return userServiceClient.findAll(true);
    }
}
