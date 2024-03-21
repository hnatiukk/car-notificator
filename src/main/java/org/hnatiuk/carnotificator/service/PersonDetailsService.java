package org.hnatiuk.carnotificator.service;

import lombok.RequiredArgsConstructor;
import org.hnatiuk.carnotificator.model.entity.Person;
import org.hnatiuk.carnotificator.repository.PersonRepository;
import org.hnatiuk.carnotificator.security.PersonDetails;
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
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = repository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Користувача з поштою %s не знайдено", email)));
        return new PersonDetails(person);
    }
}
