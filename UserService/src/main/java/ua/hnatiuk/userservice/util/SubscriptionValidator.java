package ua.hnatiuk.userservice.util;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.hnatiuk.userservice.model.entity.Subscription;

/**
 * @author Hnatiuk Volodymyr on 23.03.2024.
 */
@Component
public class SubscriptionValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Subscription.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Subscription subscription = (Subscription) target;
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
