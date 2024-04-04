package ua.hnatiuk.userservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.repository.PeopleRepository;
import ua.hnatiuk.userservice.utils.DataUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * @author Hnatiuk Volodymyr on 04.04.2024.
 */
@ExtendWith(MockitoExtension.class)
public class PeopleServiceTest {
    @Mock
    private PeopleRepository peopleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private PeopleService serviceUnderTest;
    // given

    // when

    // then
    @Test
    @DisplayName("Test register person functionality")
    public void givenPersonToRegister_whenRegister_thenRepositoryIsCalled() {
        // given
        Person personToRegister = DataUtils.getPersonTransient();
        BDDMockito.given(peopleRepository.save(any(Person.class)))
                .willReturn(DataUtils.getPersonPersisted());

        // when
        Person savedPerson = serviceUnderTest.register(personToRegister);

        // then
        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getPassword()).isNotEqualTo(personToRegister.getPassword());
        assertThat(savedPerson.getRole()).isEqualTo("ROLE_USER");
    }
}