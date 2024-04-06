package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.repository.SubscriptionsRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Hnatiuk Volodymyr on 22.03.2024.
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info("Successfully added new subscription for {}", principal.getName());
    }

    @Transactional(readOnly = true)
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
        log.info("Successfully updated subscription with id {}", subscription.getId());
    }

    @Transactional
    public void disable(Subscription subscription) {
        subscription.setIsActive(false);
        repository.save(subscription);
        log.info("Successfully disabled subscription with id {}", subscription.getId());
    }

    @Transactional
    public void activate(Subscription subscription) {
        subscription.setIsActive(true);
        repository.save(subscription);
        log.info("Successfully activated subscription with id {}", subscription.getId());
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
        log.info("Successfully deleted subscription with id {}", id);
    }

    @Transactional(readOnly = true)
    public List<Subscription> findAll(Boolean onlyActive) {
        return repository.findAllByIsActive(onlyActive);
    }
}
