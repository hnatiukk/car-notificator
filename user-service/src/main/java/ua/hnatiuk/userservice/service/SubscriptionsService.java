package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.repository.SubscriptionsRepository;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

/**
 * @author Hnatiuk Volodymyr on 22.03.2024.
 */
@Service
@RequiredArgsConstructor
public class SubscriptionsService {
    private final SubscriptionsRepository repository;
    private final PeopleService peopleService;
    private final JsonLoaderService jsonLoaderService;

    public Set<String> getBrands() {
        return jsonLoaderService.getBrands().keySet();
    }

    public Set<String> getModels() {
        return jsonLoaderService.getModels().keySet();
    }
    @Transactional
    public void addSubscription(Subscription subscription, Principal principal) {
        subscription.setIsActive(true);
        subscription.setBrandId(jsonLoaderService.getBrands().get(subscription.getBrand()));
        subscription.setModelId(jsonLoaderService.getModels().get(subscription.getModel()));
        Person owner = peopleService.findByEmailAndInitSubscriptions(principal.getName());
        subscription.setOwner(owner);
        owner.getSubscriptions().add(subscription);

        repository.save(subscription);
    }

    public Optional<Subscription> findById(Long id) {
        return repository.findById(id);
    }
    @Transactional
    public void update(Subscription subscription) {
        repository.updateSubscription(subscription.getId(),
                subscription.getBrand(),
                subscription.getModel(),
                subscription.getPriceStart(),
                subscription.getPriceEnd(),
                subscription.getYearStart(),
                subscription.getYearEnd(),
                subscription.getMileageStart(),
                subscription.getMileageEnd(),
                subscription.getTransmissionType(),
                subscription.getFuelType(),
                jsonLoaderService.getBrands().get(subscription.getBrand()),
                jsonLoaderService.getModels().get(subscription.getModel()));
    }

    public void disable(Subscription subscription) {
        subscription.setIsActive(false);
        repository.save(subscription);
    }

    public void activate(Subscription subscription) {
        subscription.setIsActive(true);
        repository.save(subscription);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
