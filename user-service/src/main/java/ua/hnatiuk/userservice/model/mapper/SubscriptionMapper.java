package ua.hnatiuk.userservice.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.hnatiuk.dto.PersonDTO;
import ua.hnatiuk.dto.SubscriptionDTO;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;

/**
 * @author Hnatiuk Volodymyr on 04.04.2024.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    @Mapping(source = "owner", target = "owner")
    SubscriptionDTO subscriptionToDTO(Subscription subscription);

    default PersonDTO personToDTO(Person person) {
        if (person == null) {
            return null;
        }

        return PersonDTO.builder()
                .email(person.getEmail())
                .tgChatId(person.getTgChatId())
                .build();
    }
}
