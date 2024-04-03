package ua.hnatiuk.userservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.utils.DataUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hnatiuk Volodymyr on 03.04.2024.
 */
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SubscriptionsRepositoryTest {
    @Autowired
    private SubscriptionsRepository subscriptionRepository;

    @BeforeEach
    public void clearData() {
        subscriptionRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save subscription functionality")
    public void givenSubscription_whenSave_thenSubscriptionIsCreated () {
        // given
        Subscription subscription = DataUtils.getSubscriptionTransient();

        // when
        Subscription savedSubscription = subscriptionRepository.save(subscription);

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
        subscriptionRepository.save(subscriptionToCreate);

        // when
        Subscription subscriptionToUpdate = subscriptionRepository.findById(subscriptionToCreate.getId())
                .orElse(null);
        subscriptionToUpdate.setPriceStart(updatedPriceStart);
        Subscription updatedSubscription = subscriptionRepository.save(subscriptionToUpdate);

        // then
        assertThat(updatedSubscription).isNotNull();
        assertThat(updatedSubscription.getPriceStart()).isEqualTo(updatedPriceStart);
    }

    @Test
    @DisplayName("Test find by subscription id functionality")
    public void givenSubscriptionId_whenFindById_thenReturnSubscription() {
        // given
        Subscription subscription = DataUtils.getSubscriptionTransient();
        subscriptionRepository.save(subscription);

        // when
        Subscription obtainedSubscription = subscriptionRepository.findById(subscription.getId()).orElse(null);

        // then
        assertThat(obtainedSubscription).isNotNull();
        assertThat(obtainedSubscription.getBrand()).isEqualTo(subscription.getBrand());
    }

    @Test
    @DisplayName("Test subscription not found functionality")
    public void givenSubscriptionIsNotCreated_whenFindById_thenOptionalEmpty() {
        // given

        // when
        Subscription obtainedSubscription = subscriptionRepository.findById(1L).orElse(null);

        // then
        assertThat(obtainedSubscription).isNull();
    }

    @Test
    @DisplayName("Test delete by subscription id functionality")
    public void givenSubscriptionIsSaved_whenDeleteById_thenSubscriptionRemovedFromDB() {
        // given
        Subscription subscription = DataUtils.getSubscriptionTransient();
        subscriptionRepository.save(subscription);

        // when
        subscriptionRepository.deleteById(subscription.getId());

        // then
        Subscription obtainedSubscription = subscriptionRepository.findById(subscription.getId()).orElse(null);
        assertThat(obtainedSubscription).isNull();
    }
}
