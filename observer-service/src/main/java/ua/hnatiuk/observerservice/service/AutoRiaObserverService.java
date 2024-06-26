package ua.hnatiuk.observerservice.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.hnatiuk.dto.MessageDTO;
import ua.hnatiuk.dto.SubscriptionDTO;
import ua.hnatiuk.observerservice.feign.AutoRiaClient;
import ua.hnatiuk.observerservice.feign.params.AutoRiaRequestParams;
import ua.hnatiuk.dto.car.CarDTO;


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
    private final NotificationService notificationService;

    /**
     * Checks for availability of new cars for sale 1 time per hour
     */
    @Scheduled(fixedRate = 60 * 60 * 1000) // 1 hour
    private void checkAll() {
        log.debug("Scheduled task started");
        List<SubscriptionDTO> subscriptions = subscriptionsService.getActiveSubscriptions();

        subscriptions.stream()
                .filter(s -> s.getOwner().getTgChatId() != null)
                .forEach(this::checkSubscription);
    }

    /**
     * Checks subscription
     * @param subscription SubscriptionDTO to check
     */
    private void checkSubscription(SubscriptionDTO subscription) {
        List<Integer> newCarIds = getNewCarIds(subscription);

        if (newCarIds.isEmpty())  {
            log.debug("Could not find any new car for subscription");
            return;
        }

        log.debug("Found {} new cars for subscription", newCarIds.size());

        List<CarDTO> newCarDTOs = getNewCarDTOs(newCarIds);

        notifyAboutNewCars(newCarDTOs, subscription.getOwner().getTgChatId());
    }

    /**
     * Returns new car ids
     * @param subscription SubscriptionDTO to get new car ids
     * @return List of ids
     */
    private List<Integer> getNewCarIds(SubscriptionDTO subscription) {
        AutoRiaRequestParams params = AutoRiaRequestParams.subscriptionToParams(subscription);
        String response = autoRiaClient.search(params, 3);
        log.debug("Sent request for subscription");

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

    /**
     * Returns new carDTOs
     * @param newCarIds List of ids to get carDTOs
     * @return List of new carDTOs
     */
    private List<CarDTO> getNewCarDTOs(List<Integer> newCarIds) {
        return newCarIds.stream()
                .map(autoRiaClient::searchCar)
                .toList();
    }

    /**
     * Notifies owner of subscription about new cars
     * @param carDTOs carDTOs to notify with
     * @param tgChatId Chat id of owner
     */
    private void notifyAboutNewCars(List<CarDTO> carDTOs, Long tgChatId) {
        for (CarDTO carDTO : carDTOs) {
            String text = formMessage(carDTO);

            MessageDTO messageDTO = new MessageDTO(tgChatId, text, carDTO.getPhotoData().getPhotoLink());
            notificationService.sendNotification(messageDTO);
        }
    }

    /**
     * Forms a message for user about new car
     * @param carDTO CarDTO to form message from
     * @return Message for owner
     */
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
