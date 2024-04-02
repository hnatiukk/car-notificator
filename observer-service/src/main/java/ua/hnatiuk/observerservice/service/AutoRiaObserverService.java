package ua.hnatiuk.observerservice.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.hnatiuk.observerservice.feign.AutoRiaClient;
import ua.hnatiuk.observerservice.feign.NotificationServiceClient;
import ua.hnatiuk.observerservice.feign.params.AutoRiaRequestParams;
import ua.hnatiuk.observerservice.model.dto.CarDTO;
import ua.hnatiuk.observerservice.model.dto.MessageDTO;
import ua.hnatiuk.observerservice.model.entity.Subscription;


import java.util.Collections;
import java.util.List;

import static java.lang.StringTemplate.STR;


/**
 * @author Hnatiuk Volodymyr on 26.03.2024.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AutoRiaObserverService {
    private final SubscriptionsService subscriptionsService;
    private final AutoRiaClient autoRiaClient;
    private final NotificationServiceClient notificationServiceClient;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    private void checkAll() {
        log.debug("Scheduled task started");
        List<Subscription> subscriptions = subscriptionsService.getActiveSubscriptions();

        subscriptions.forEach(this::checkSubscription);
    }

    private void checkSubscription(Subscription subscription) {
        List<Integer> newCarIds = getNewCarIds(subscription);

        if (newCarIds.isEmpty())  {
            log.debug("Could not find any new car for subscription with id {}", subscription.getId());
            return;
        }

        log.debug("Found {} new cars for subscription with id {}", newCarIds.size(), subscription.getId());

        List<CarDTO> newCarDTOs = getNewCarDTOs(newCarIds);

        notifyAboutNewCars(newCarDTOs, subscription.getOwner().getTgChatId());
    }

    private List<CarDTO> getNewCarDTOs(List<Integer> newCarIds) {
        return newCarIds.stream()
                .map(autoRiaClient::searchCar)
                .toList();
    }

    private List<Integer> getNewCarIds(Subscription subscription) {
        AutoRiaRequestParams params = AutoRiaRequestParams.subscriptionToParams(subscription);
        String response = autoRiaClient.search(params, 3);
        log.debug("Sent request for subscription with id {}", subscription.getId());

        JsonObject responseObject = new Gson().fromJson(response, JsonObject.class);

        JsonArray jsonCarIds = responseObject
                .get("result").getAsJsonObject()
                .get("search_result").getAsJsonObject()
                .get("ids").getAsJsonArray();

        if (jsonCarIds.isEmpty()) return Collections.emptyList();

        return jsonCarIds.asList()
                .stream()
                .map(JsonElement::getAsInt)
                .toList();
    }

    private void notifyAboutNewCars(List<CarDTO> carDTOs, Long tgChatId) {
        for (CarDTO carDTO : carDTOs) {
            String text = formMessage(carDTO);

            MessageDTO messageDTO = new MessageDTO(tgChatId, text, carDTO.getPhotoData().getPhotoLink());
            notificationServiceClient.sendNotification(messageDTO);
            log.debug("Sent notification to chat id {}", tgChatId);
        }
    }

    private String formMessage(CarDTO carDTO) {
        return STR."""
                        Знайдено нову пропозицію!

                        \{carDTO.getBrand()} \{carDTO.getModel()} \{carDTO.getAutoData().getYear()}

                        💵 Ціна: \{carDTO.getPrice()} $
                        🕹 Коробка: \{carDTO.getAutoData().getTransmission()}
                        ⚙️ Двигун: \{carDTO.getAutoData().getFuelType()}
                        🛣 Пробіг: \{carDTO.getAutoData().getMileage()}
                        🌎 Місцезнаходження: \{carDTO.getStateData().getCity()}, \{carDTO.getStateData().getRegion()}

                        Посилання на оглошення: auto.ria.com/uk\{carDTO.getLink()}
                        """;
    }

}
