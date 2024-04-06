package ua.hnatiuk.userservice.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.utils.DataUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hnatiuk Volodymyr on 03.04.2024.
 */
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SubscriptionsRepositoryTest {
    @Autowired
    private SubscriptionsRepository subscriptionsRepository;
    @Autowired
    private PeopleRepository peopleRepository;

    @AfterEach
    public void clearData() {
        subscriptionsRepository.deleteAll();
        peopleRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save subscription functionality")
    public void givenSubscription_whenSave_thenSubscriptionIsCreated () {
        // given
        Subscription subscription = DataUtils.getSubscriptionTransient();

        // when
        Subscription savedSubscription = subscriptionsRepository.save(subscription);

        // then
        assertThat(savedSubscription).isNotNull();
        assertThat(savedSubscription.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test update subscription functionality")
    public void givenSubscriptionToUpdate_whenUSave_thenAttributesAreUpdated() {
        // given
        Integer updatedPriceStart = 5000;
        Subscription subscriptionToCreate = DataUtils.getSubscriptionTransient();
        subscriptionsRepository.save(subscriptionToCreate);

        // when
        Subscription subscriptionToUpdate = subscriptionsRepository.findById(subscriptionToCreate.getId())
                .orElse(null);
        subscriptionToUpdate.setPriceStart(updatedPriceStart);
        Subscription updatedSubscription = subscriptionsRepository.save(subscriptionToUpdate);

        // then
        assertThat(updatedSubscription).isNotNull();
        assertThat(updatedSubscription.getPriceStart()).isEqualTo(updatedPriceStart);
    }

    @Test
    @DisplayName("Test find by subscription id functionality")
    public void givenSubscriptionId_whenFindById_thenReturnSubscription() {
        // given
        Subscription subscription = DataUtils.getSubscriptionTransient();
        subscriptionsRepository.save(subscription);

        // when
        Subscription obtainedSubscription = subscriptionsRepository.findById(subscription.getId()).orElse(null);

        // then
        assertThat(obtainedSubscription).isNotNull();
        assertThat(obtainedSubscription.getBrand()).isEqualTo(subscription.getBrand());
    }

    @Test
    @DisplayName("Test subscription not found functionality")
    public void givenSubscriptionIsNotCreated_whenFindById_thenOptionalEmpty() {
        // given

        // when
        Subscription obtainedSubscription = subscriptionsRepository.findById(1L).orElse(null);

        // then
        assertThat(obtainedSubscription).isNull();
    }

    @Test
    @DisplayName("Test find all subscriptions functionality")
    public void givenSubscriptions_whenSaveAll_thenReturnAllSubscriptions() {
        // given
        Person owner = DataUtils.getPersonPersisted();
        peopleRepository.save(owner);

        Subscription subscription1 = DataUtils.getSubscriptionTransient();
        Subscription subscription2 = DataUtils.getSubscriptionTransient();
        Subscription subscription3 = DataUtils.getSubscriptionTransient();

        List<Subscription> subscriptionList = List.of(subscription1, subscription2, subscription3);
        subscriptionList.forEach(s -> {
            s.setOwner(owner);
            owner.getSubscriptions().add(s);
        });

        subscriptionsRepository.saveAll(subscriptionList);

        // when
        List<Subscription> obtainedList = subscriptionsRepository.findAll();

        // then
        assertThat(obtainedList).isNotEmpty();
        assertThat(obtainedList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test find all by active subscriptions functionality")
    public void givenSubscriptions_whenSaveAll_thenReturnAllOnlyActiveSubscriptions() {
        // given
        Person owner = DataUtils.getPersonPersisted();
        peopleRepository.save(owner);

        Subscription subscription1 = DataUtils.getSubscriptionTransient();
        Subscription subscription2 = DataUtils.getSubscriptionTransient();
        Subscription subscription3 = DataUtils.getSubscriptionTransient();

        subscription2.setIsActive(false);

        List<Subscription> subscriptionList = List.of(subscription1, subscription2, subscription3);
        subscriptionList.forEach(s -> s.setOwner(owner));

        subscriptionsRepository.saveAll(subscriptionList);

        // when
        List<Subscription> obtainedList = subscriptionsRepository.findAllByIsActive(true);

        // then
        assertThat(obtainedList).isNotEmpty();
        assertThat(obtainedList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test delete by subscription id functionality")
    public void givenSubscriptionIsSaved_whenDeleteById_thenSubscriptionRemovedFromDB() {
        // given
        Subscription subscription = DataUtils.getSubscriptionTransient();
        subscriptionsRepository.save(subscription);

        // when
        subscriptionsRepository.deleteById(subscription.getId());

        // then
        Subscription obtainedSubscription = subscriptionsRepository.findById(subscription.getId()).orElse(null);
        assertThat(obtainedSubscription).isNull();
    }
}
