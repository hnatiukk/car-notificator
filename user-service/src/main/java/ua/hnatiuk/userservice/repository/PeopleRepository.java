package ua.hnatiuk.userservice.repository;

import ua.hnatiuk.userservice.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
public interface PeopleRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
}
