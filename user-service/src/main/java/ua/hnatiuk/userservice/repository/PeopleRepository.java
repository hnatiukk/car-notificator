package ua.hnatiuk.userservice.repository;

import org.springframework.stereotype.Repository;
import ua.hnatiuk.userservice.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
}
