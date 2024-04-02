package ua.hnatiuk.observerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnatiuk.observerservice.model.entity.Subscription;
import ua.hnatiuk.observerservice.repository.SubscriptionsRepository;

import java.util.List;

/**
 * @author Hnatiuk Volodymyr on 26.03.2024.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SubscriptionsService {
    private final SubscriptionsRepository repository;

    public List<Subscription> getActiveSubscriptions() {
        log.debug("Getting all active subscriptions");
        return repository.findAllByIsActiveTrue();
    }
}
