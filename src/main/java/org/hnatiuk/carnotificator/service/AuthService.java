package org.hnatiuk.carnotificator.service;

import lombok.RequiredArgsConstructor;
import org.hnatiuk.carnotificator.exception.InvalidCredentialsException;
import org.hnatiuk.carnotificator.model.dto.PersonDTO;
import org.hnatiuk.carnotificator.model.entity.Person;
import org.hnatiuk.carnotificator.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final PersonRepository repository;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public void authenticateByDTO(PersonDTO personDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(personDTO.getEmail(),
                        personDTO.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException exception) {
            throw new InvalidCredentialsException(exception.getLocalizedMessage());
        }
    }

    public Person personDTOToPerson(PersonDTO personDTO) {
        return mapper.map(personDTO, Person.class);
    }

    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        repository.save(person);
    }
}
