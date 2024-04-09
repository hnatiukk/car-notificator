package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.repository.SubscriptionsRepository;

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

    /**
     * Returns brands set
     * @return Brands set
     */
    public Set<String> getBrands() {
        return jsonLoaderService.getBrands().keySet();
    }

    /**
     * Returns models set
     * @return Models set
     */
    public Set<String> getModels() {
        return jsonLoaderService.getModels().keySet();
    }

    /**
     * Adds new subscription
     * @param subscription Subscription entity to add
     * @param email Email of owner
     * @return Saved subscription entity
     */
    @Transactional
    public Subscription addSubscription(Subscription subscription, String email) {
        subscription.setIsActive(true);
        subscription.setBrandId(jsonLoaderService.getBrands().get(subscription.getBrand()));
        subscription.setModelId(jsonLoaderService.getModels().get(subscription.getModel()));
        Person owner = peopleService.findByEmailAndInitSubscriptions(email);
        subscription.setOwner(owner);
        owner.getSubscriptions().add(subscription);

        log.info("Successfully added new subscription for {}", email);
        return repository.save(subscription);
    }

    /**
     * Finds subscription by id
     * @param id Id of subscription to find
     * @return Optional of found subscription
     */
    @Transactional(readOnly = true)
    public Optional<Subscription> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Updates subscription
     * @param subscription Updated subscription entity
     */
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

    /**
     * Disables subscription
     * @param subscription Subscription to disable
     */
    @Transactional
    public void disable(Subscription subscription) {
        subscription.setIsActive(false);
        repository.save(subscription);
        log.info("Successfully disabled subscription with id {}", subscription.getId());
    }

    /**
     * Activates subscription
     * @param subscription Subscription to activate
     */
    @Transactional
    public void activate(Subscription subscription) {
        subscription.setIsActive(true);
        repository.save(subscription);
        log.info("Successfully activated subscription with id {}", subscription.getId());
    }

    /**
     * Deletes subscription
     * @param id Id of subscription to delete
     */
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
        log.info("Successfully deleted subscription with id {}", id);
    }

    /**
     * Finds all subscription by its active status
     * @param onlyActive Status of subscriptions
     * @return List of found subscriptions
     */
    @Transactional(readOnly = true)
    public List<Subscription> findAllByIsActive(Boolean onlyActive) {
        return repository.findAllByIsActive(onlyActive);
    }
}
