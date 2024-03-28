package ua.hnatiuk.observerservice.feign.params;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.hnatiuk.observerservice.model.entity.Subscription;

/**
 * @author Hnatiuk Volodymyr on 27.03.2024.
 */
@Getter
@Setter
@Builder
public class AutoRiaRequestParams {
    private Integer category_id;
    private Integer marka_id;
    private Integer model_id;
    private Integer price_ot;
    private Integer price_do;
    private Integer s_yers;
    private Integer po_yers;
    private Integer raceFrom;
    private Integer raceTo;
    private Integer gearbox;
    private Integer type;
    public static AutoRiaRequestParams subscriptionToParams(Subscription subscription) {
        return AutoRiaRequestParams.builder()
                .category_id(1)
                .marka_id(subscription.getBrandId())
                .model_id(subscription.getModelId())
                .price_ot(subscription.getPriceStart())
                .price_do(subscription.getPriceEnd())
                .s_yers(subscription.getYearStart())
                .po_yers(subscription.getYearEnd())
                .raceFrom(subscription.getMileageStart())
                .raceTo(subscription.getMileageEnd())
                .gearbox(subscription.getTransmissionType().ordinal() + 1)
                .type(subscription.getFuelType().ordinal() + 1)
                .build();
    }
}
