package ua.hnatiuk.observerservice.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.hnatiuk.observerservice.feign.AutoRiaClient;
import ua.hnatiuk.observerservice.feign.NotificationServiceClient;
import ua.hnatiuk.observerservice.feign.params.AutoRiaRequestParams;
import ua.hnatiuk.observerservice.model.dto.CarDTO;
import ua.hnatiuk.observerservice.model.entity.Subscription;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.StringTemplate.STR;


/**
 * @author Hnatiuk Volodymyr on 26.03.2024.
 */
@Service
@RequiredArgsConstructor
public class AutoRiaObserverService {
    private final SubscriptionsService subscriptionsService;
    private final AutoRiaClient autoRiaClient;
    private final NotificationServiceClient notificationServiceClient;

    @Scheduled(fixedRate = 61 * 60 * 1000)
    private void checkAll() {
        List<Subscription> subscriptions = subscriptionsService.getActiveSubscriptions();

        subscriptions.forEach(this::checkSubscription);
    }
    //@Async
    private void checkSubscription(Subscription subscription) {
        List<Integer> newCarIds = getNewCarIds(subscription);

        if (newCarIds.isEmpty()) return;

        List<CarDTO> newCarDTOs = getNewCarDTOs(newCarIds);

        notifyAboutNewCars(newCarDTOs, subscription.getOwner().getTgChatId());
    }

    private void notifyAboutNewCars(List<CarDTO> carDTOs, Long tgChatId) {
        for (CarDTO carDTO : carDTOs) {
            String message = formMessage(carDTO);

            notificationServiceClient.sendNotification(tgChatId, message, carDTO.getPhotoData().getPhotoLink());
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

    private List<CarDTO> getNewCarDTOs(List<Integer> newCarIds) {
        List<CarDTO> newCarDTOs = new ArrayList<>(newCarIds.size());

        return newCarIds.stream()
                .map(autoRiaClient::searchCar)
                .toList();
    }

    private List<Integer> getNewCarIds(Subscription subscription) {
        AutoRiaRequestParams params = AutoRiaRequestParams.subscriptionToParams(subscription);
        String response = autoRiaClient.search(params, 3);

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
}
