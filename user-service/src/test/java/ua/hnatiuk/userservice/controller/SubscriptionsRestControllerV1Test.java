package ua.hnatiuk.userservice.controller;

import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.hnatiuk.userservice.config.SecurityConfig;
import ua.hnatiuk.userservice.controller.rest.SubscriptionsRestController;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.model.mapper.SubscriptionMapper;
import ua.hnatiuk.userservice.service.SubscriptionsService;
import ua.hnatiuk.userservice.utils.DataUtils;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author Hnatiuk Volodymyr on 07.04.2024.
 */
@WebMvcTest(SubscriptionsRestController.class)
@Import(SecurityConfig.class)
public class SubscriptionsRestControllerV1Test {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubscriptionMapper subscriptionMapper;
    @MockBean
    private SubscriptionsService subscriptionsService;
    @Value("${carnotificator.inner-api-key}")
    private String innerApiKey;

    @Test
    @DisplayName("Test get subscriptions functionality")
    @SneakyThrows
    public void givenOnlyActive_whenGetSubscriptions_thenReturnOnlyActiveSubscriptions() {
        // given
        boolean onlyActive = true;
        Subscription subscription1 = DataUtils.getSubscriptionPersisted();
        Subscription subscription2 = DataUtils.getSubscriptionPersisted();
        Subscription subscription3 = DataUtils.getSubscriptionPersisted();

        subscription2.setId(2L);
        subscription3.setId(3L);

        subscription2.setIsActive(false);

        BDDMockito.given(subscriptionsService.findAllByIsActive(true))
                .willReturn(List.of(subscription1, subscription3));
        BDDMockito.given(subscriptionMapper.subscriptionToDTO(any(Subscription.class)))
                .willReturn(DataUtils.getSubscriptionDTO());

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/subscriptions")
                .param("inner_key", innerApiKey)
                .param("active", Boolean.toString(onlyActive))
        );

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brand").value("BMW"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isActive").value("true"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isActive").value("true"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].owner", CoreMatchers.notNullValue()));
    }

    @Test
    @DisplayName("Test get subscriptions with invalid inner key functionality")
    @SneakyThrows
    public void givenInvalidInnerKey_whenGetSubscriptions_thenForbidden() {
        // given
        String invalidInnerKey = "invalid-key52736";

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/subscriptions")
                .param("inner_key", invalidInnerKey)
                .param("active", Boolean.toString(true))
        );

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
