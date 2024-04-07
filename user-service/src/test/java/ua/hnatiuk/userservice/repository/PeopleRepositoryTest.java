package ua.hnatiuk.userservice.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.utils.DataUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hnatiuk Volodymyr on 03.04.2024.
 */
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PeopleRepositoryTest {
    @Autowired
    private PeopleRepository peopleRepository;

    @AfterEach
    public void clearData() {
        peopleRepository.deleteAll();
    }
    @Test
    @DisplayName("Test save person functionality")
    public void givenPerson_whenSave_thenPersonIsCreated () {
        // given
        Person person = DataUtils.getPersonTransient();

        // when
        Person savedPerson = peopleRepository.save(person);

        // then
        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getId()).isGreaterThan(0L);
    }
    @Test
    @DisplayName("Test update person functionality")
    public void givenPersonToUpdate_whenUSave_thenTgChatIdIsUpdated() {
        // given
        Long updatedTgChatId = 7632884L;
        Person personToCreate = DataUtils.getPersonTransient();
        peopleRepository.save(personToCreate);

        // when
        Person personToUpdate = peopleRepository.findById(personToCreate.getId())
                .orElse(null);
        personToUpdate.setTgChatId(updatedTgChatId);
        Person updatedPerson = peopleRepository.save(personToUpdate);

        // then
        assertThat(updatedPerson).isNotNull();
        assertThat(personToCreate.getTgChatId()).isEqualTo(updatedTgChatId);
    }

    @Test
    @DisplayName("Test find by person id functionality")
    public void givenPersonId_whenFindById_thenReturnPerson() {
        // given
        Person person = DataUtils.getPersonTransient();
        peopleRepository.save(person);

        // when
        Person obtainedPerson = peopleRepository.findById(person.getId()).orElse(null);

        // then
        assertThat(obtainedPerson).isNotNull();
        assertThat(obtainedPerson.getEmail()).isEqualTo(person.getEmail());
    }

    @Test
    @DisplayName("Test person not found functionality")
    public void givenPersonIsNotCreated_whenFindById_thenOptionalEmpty() {
        // given

        // when
        Person obtainedPerson = peopleRepository.findById(1L).orElse(null);

        // then
        assertThat(obtainedPerson).isNull();
    }

    @Test
    @DisplayName("Test find by person email functionality")
    public void givenPersonEmail_whenFindByEmail_thenReturnPerson() {
        // given
        Person person = DataUtils.getPersonTransient();
        peopleRepository.save(person);

        // when
        Person obtainedPerson = peopleRepository.findByEmail(person.getEmail()).orElse(null);

        // then
        assertThat(obtainedPerson).isNotNull();
        assertThat(obtainedPerson.getEmail()).isEqualTo(person.getEmail());
    }

    @Test
    @DisplayName("Test delete by person id functionality")
    public void givenPersonIsSaved_whenDeleteById_thenPersonRemovedFromDB() {
        // given
        Person person = DataUtils.getPersonTransient();
        peopleRepository.save(person);

        // when
        peopleRepository.deleteById(person.getId());

        // then
        Person obtainedPerson = peopleRepository.findById(person.getId()).orElse(null);
        assertThat(obtainedPerson).isNull();
    }
}