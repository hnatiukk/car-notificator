package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.repository.SubscriptionsRepository;

import java.util.List;

/**
 * @author Hnatiuk Volodymyr on 22.03.2024.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubscriptionsService {
    private final SubscriptionsRepository repository;
    private final PeopleService peopleService;

    public List<Subscription> findAllByEmail(String email) {
        Person person = peopleService.findByEmailAndInitSubscriptions(email);

        return person.getSubscriptions();
    }
}
