package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.repository.SubscriptionsRepository;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Hnatiuk Volodymyr on 22.03.2024.
 */
@Service
@RequiredArgsConstructor
public class SubscriptionsService {
    private final SubscriptionsRepository repository;
    private final PeopleService peopleService;
    private final JsonLoaderService jsonLoaderService;
    @Transactional(readOnly = true)
    public List<Subscription> findAllByEmail(String email) {
        Person person = peopleService.findByEmailAndInitSubscriptions(email);

        return person.getSubscriptions();
    }

    public Set<String> getBrands() {
        return jsonLoaderService.getBrands().keySet();
    }

    public Set<String> getModels() {
        return jsonLoaderService.getModels().keySet();
    }
    @Transactional
    public void addSubscription(Subscription subscription, Principal principal) {
        subscription.setIsActive(true);
        subscription.setRequestParams(generateRequestParams(subscription));
        Person owner = peopleService.findByEmailAndInitSubscriptions(principal.getName());
        subscription.setOwner(owner);
        owner.getSubscriptions().add(subscription);

        repository.save(subscription);
    }

    private String generateRequestParams(Subscription subscription) {
        Map<String, Integer> brands = jsonLoaderService.getBrands();
        Map<String, Integer> models = jsonLoaderService.getModels();
        StringBuilder params = new StringBuilder();

        params.append("&category_id=1");
        params.append("&marka_id=").append(brands.get(subscription.getBrand()));
        params.append("&model_id=").append(models.get(subscription.getModel()));
        if (subscription.getPriceStart() != null) {
            params.append("&price_ot=").append(subscription.getPriceStart());
        }
        if (subscription.getPriceEnd() != null) {
            params.append("&price_do=").append(subscription.getPriceEnd());
        }
        if (subscription.getYearStart() != null) {
            params.append("&s_yers=").append(subscription.getYearStart());
        }
        if (subscription.getYearEnd() != null) {
            params.append("&po_yers=").append(subscription.getYearEnd());
        }
        if (subscription.getMileageStart() != null) {
            params.append("&raceFrom=").append(subscription.getMileageStart());
        }
        if (subscription.getMileageEnd() != null) {
            params.append("&raceTo=").append(subscription.getMileageEnd());
        }
        if (subscription.getTransmissionType() != null) {
            params.append("&gearbox=").append(subscription.getTransmissionType().ordinal() + 1);
        }
        if (subscription.getFuelType() != null) {
            params.append("&type=").append(subscription.getFuelType().ordinal() + 1);
        }

        return params.toString();
    }
}
