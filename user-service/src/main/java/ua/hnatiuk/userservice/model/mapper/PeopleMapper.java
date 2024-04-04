package ua.hnatiuk.userservice.model.mapper;

import org.mapstruct.Mapper;
import ua.hnatiuk.dto.PersonDTO;
import ua.hnatiuk.userservice.model.entity.Person;

/**
 * @author Hnatiuk Volodymyr on 04.04.2024.
 */
@Mapper(componentModel = "spring")
public interface PeopleMapper {
    PersonDTO personToDTO(Person person);
}
