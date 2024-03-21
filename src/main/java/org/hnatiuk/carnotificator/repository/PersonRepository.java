package org.hnatiuk.carnotificator.repository;

import org.hnatiuk.carnotificator.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
