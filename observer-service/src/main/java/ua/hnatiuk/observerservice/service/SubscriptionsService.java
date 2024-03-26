package ua.hnatiuk.observerservice.service;

import lombok.RequiredArgsConstructor;
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
public class SubscriptionsService {
    private final SubscriptionsRepository repository;

    public List<Subscription> getActiveSubscriptions() {
        return repository.findAllByIsActiveTrue();
    }
}
