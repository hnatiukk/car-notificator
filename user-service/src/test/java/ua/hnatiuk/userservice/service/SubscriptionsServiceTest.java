package ua.hnatiuk.userservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.repository.SubscriptionsRepository;
import ua.hnatiuk.userservice.utils.DataUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Hnatiuk Volodymyr on 06.04.2024.
 */
@ExtendWith(MockitoExtension.class)
public class SubscriptionsServiceTest {
    @Mock
    private SubscriptionsRepository subscriptionsRepository;
    @Mock
    private PeopleService peopleService;
    @Mock
    private JsonLoaderService jsonLoaderService;
    @InjectMocks
    private SubscriptionsService serviceUnderTest;

    @Test
    @DisplayName("Test find by id subscription functionality")
    public void givenSubscriptionId_whenFindById_thenReturnSubscription() {
        // given
        Long id = 1L;
        BDDMockito.given(subscriptionsRepository.findById(anyLong()))
                .willReturn(Optional.of(DataUtils.getSubscriptionPersisted()));

        // when
        Optional<Subscription> obtainedSubscriptionOptional = serviceUnderTest.findById(id);

        // then
        assertThat(obtainedSubscriptionOptional).isNotEmpty();
        assertThat(obtainedSubscriptionOptional.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Test find by incorrect id subscription functionality")
    public void givenIncorrectSubscriptionId_whenFindById_thenReturnEmptyOptional() {
        // given
        Long id = 1L;
        BDDMockito.given(subscriptionsRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        Optional<Subscription> obtainedSubscriptionOptional = serviceUnderTest.findById(id);

        // then
        assertThat(obtainedSubscriptionOptional).isEmpty();
    }

    @Test
    @DisplayName("Test add subscription functionality")
    public void givenSubscriptionToSave_whenSave_thenSubscriptionIsSaved() {
        // given
        String email = "test@gmail.com";
        Subscription subscriptionToSave = DataUtils.getSubscriptionTransient();

        BDDMockito.given(subscriptionsRepository.save(any(Subscription.class)))
                .willReturn(DataUtils.getSubscriptionPersisted());
        BDDMockito.given(peopleService.findByEmailAndInitSubscriptions(anyString()))
                .willReturn(DataUtils.getPersonPersisted());

        // when
        Subscription savedSubscription = serviceUnderTest.addSubscription(subscriptionToSave, email);

        // then
        assertThat(savedSubscription).isNotNull();
        assertThat(savedSubscription.getIsActive()).isTrue();
        assertThat(savedSubscription.getBrandId()).isNotNull();
        assertThat(savedSubscription.getModelId()).isNotNull();
        assertThat(savedSubscription.getOwner()).isNotNull();
    }

    @Test
    @DisplayName("Test disable subscription functionality")
    public void givenSubscriptionToDisable_whenDisable_thenSubscriptionIsDisabled() {
        // given
        Subscription subscriptionToDisable = DataUtils.getSubscriptionPersisted();

        // when
        serviceUnderTest.disable(subscriptionToDisable);

        // then
        verify(subscriptionsRepository, times(1)).save(any(Subscription.class));
        verify(subscriptionsRepository, never()).deleteById(anyLong());
        assertThat(subscriptionToDisable.getIsActive()).isFalse();
    }

    @Test
    @DisplayName("Test activate subscription functionality")
    public void givenSubscriptionToActivate_whenActivate_thenSubscriptionIsActivated() {
        // given
        Subscription subscriptionToActivate = DataUtils.getSubscriptionPersisted();
        subscriptionToActivate.setIsActive(false);

        // when
        serviceUnderTest.activate(subscriptionToActivate);

        // then
        verify(subscriptionsRepository, times(1)).save(any(Subscription.class));
        assertThat(subscriptionToActivate.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Test delete by id subscription functionality")
    public void givenSubscriptionId_whenDelete_thenSubscriptionIsDeleted() {
        // given
        Long id = 1L;

        // when
        serviceUnderTest.deleteById(id);

        // then
        verify(subscriptionsRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test find all by isActive subscription functionality")
    public void givenTwoSubscriptions_whenFindByIsActive_thenReturnOnlyActiveSubscriptions() {
        // given
        Subscription subscription1 = DataUtils.getSubscriptionPersisted();
        Subscription subscription2 = DataUtils.getSubscriptionPersisted();

        subscription2.setIsActive(false);

        BDDMockito.given(subscriptionsRepository.findAllByIsActive(true))
                .willReturn(List.of(subscription1));

        // when
        List<Subscription> subscriptionList = serviceUnderTest.findAllByIsActive(true);

        // then
        verify(subscriptionsRepository, times(1)).findAllByIsActive(anyBoolean());
        assertThat(subscriptionList).isNotEmpty();
        assertThat(subscriptionList.size()).isEqualTo(1);
    }
}