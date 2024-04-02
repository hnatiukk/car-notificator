package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.repository.PeopleRepository;
import ua.hnatiuk.userservice.security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = repository.findByEmail(email).orElseThrow(() ->
                {
                    log.error("Could not find user with email {}", email);
                    return new UsernameNotFoundException(String.format("Користувача з поштою %s не знайдено", email));
                }
        );
        return new PersonDetails(person);
    }
}
