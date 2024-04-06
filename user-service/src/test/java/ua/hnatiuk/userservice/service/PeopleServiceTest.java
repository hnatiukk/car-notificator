package ua.hnatiuk.userservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.hnatiuk.userservice.exception.EmailNotFoundException;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.repository.PeopleRepository;
import ua.hnatiuk.userservice.utils.DataUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    @DisplayName("Test find person by email functionality")
    public void givenEmail_whenFindByEmail_thenReturnPerson() {
        // given
        String email = "test@gmail.com";
        BDDMockito.given(serviceUnderTest.findByEmail(anyString()))
                .willReturn(Optional.of(DataUtils.getPersonPersisted()));

        // when
        Person obtainedPerson = serviceUnderTest.findByEmail(email).get();

        // then
        assertThat(obtainedPerson).isNotNull();
        assertThat(obtainedPerson.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Test find person by incorrect email functionality")
    public void givenIncorrectEmail_whenFindByEmail_thenReturnEmptyOptional() {
        // given
        String email = "test@gmail.com";
        BDDMockito.given(serviceUnderTest.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        Optional<Person> obtainedPersonOptional = serviceUnderTest.findByEmail(email);

        // then
        assertThat(obtainedPersonOptional).isEmpty();
    }

    @Test
    @DisplayName("Test assign chat id functionality")
    public void givenEmailAndChatId_whenAssignChatId_thenChatIdIsAssigned() {
        // given
        String email = "test@gmail.com";
        Long chatId = 4190192L;
        BDDMockito.given(serviceUnderTest.findByEmail(anyString()))
                .willReturn(Optional.of(DataUtils.getPersonPersisted()));

        // when
        serviceUnderTest.assignChatId(email, chatId);
        Person obtainedPerson = serviceUnderTest.findByEmail(email).get();

        // then
        assertThat(obtainedPerson.getTgChatId()).isNotNull();
        assertThat(obtainedPerson.getTgChatId()).isEqualTo(chatId);
    }

    @Test
    @DisplayName("Test assign chat id to incorrect email functionality")
    public void givenIncorrectEmailAndChatId_whenAssignChatId_thenExceptionIsThrown() {
        // given
        String email = "test@gmail.com";
        Long chatId = 4190192L;
        BDDMockito.given(serviceUnderTest.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when
        assertThrows(EmailNotFoundException.class, () -> serviceUnderTest.assignChatId(email, chatId));

        // then
    }
}