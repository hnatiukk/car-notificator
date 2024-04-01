package ua.hnatiuk.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnatiuk.notificationservice.model.entity.Person;

import java.util.Optional;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
public interface PeopleRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
}
