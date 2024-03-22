package ua.hnatiuk.userservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.service.PeopleService;

/**
 * @author Hnatiuk Volodymyr on 22.03.2024.
 */
@Component
@RequiredArgsConstructor
public class PersonValidator implements Validator {
    private final PeopleService service;

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (service.findByEmail(person.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "Такий email вже зареєстровано");
        }
    }
}