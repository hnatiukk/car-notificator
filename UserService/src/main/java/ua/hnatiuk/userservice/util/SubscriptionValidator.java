package ua.hnatiuk.userservice.util;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.service.JsonLoaderService;

/**
 * @author Hnatiuk Volodymyr on 23.03.2024.
 */
@Component
@RequiredArgsConstructor
public class SubscriptionValidator implements Validator {
    private final JsonLoaderService jsonLoaderService;
    @Override
    public boolean supports(Class<?> clazz) {
        return Subscription.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Subscription subscription = (Subscription) target;
        if (!jsonLoaderService.getBrands().containsKey(subscription.getBrand())) {
            errors.rejectValue("brand", "", "Такої марки не існує");
        }
        if (!jsonLoaderService.getModels().containsKey(subscription.getModel())) {
            errors.rejectValue("model", "", "Такої моделі не існує");
        }
        if (subscription.getPriceStart() != null && subscription.getPriceEnd() != null) {
            if (subscription.getPriceStart() >= subscription.getPriceEnd()) {
                errors.rejectValue("priceStart", "", "Повинна бути менше");
            }
        }
        if (subscription.getYearStart() != null && subscription.getYearEnd() != null) {
            if (subscription.getYearStart() >= subscription.getYearEnd()) {
                errors.rejectValue("yearStart", "", "Повиний бути менше");
            }
        }
        if (subscription.getMileageStart() != null && subscription.getYearEnd() != null) {
            if (subscription.getMileageStart() >= subscription.getMileageEnd()) {
                errors.rejectValue("mileageStart", "", "Повиний бути менше");
            }
        }
    }
}
