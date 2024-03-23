package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.repository.SubscriptionsRepository;

import java.util.List;
import java.util.Set;

/**
 * @author Hnatiuk Volodymyr on 22.03.2024.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubscriptionsService {
    private final SubscriptionsRepository repository;
    private final PeopleService peopleService;
    private final JsonLoaderService jsonLoaderService;

    public List<Subscription> findAllByEmail(String email) {
        Person person = peopleService.findByEmailAndInitSubscriptions(email);

        return person.getSubscriptions();
    }
    public Set<String> getBrands() {
        return jsonLoaderService.getBrands().keySet();
    }

    public Set<String> getModels() {
        return jsonLoaderService.getModels().keySet();
    }
}
