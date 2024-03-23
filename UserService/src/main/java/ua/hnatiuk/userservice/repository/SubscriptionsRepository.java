package ua.hnatiuk.userservice.repository;

import ua.hnatiuk.userservice.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
public interface SubscriptionsRepository extends JpaRepository<Subscription, Long> {
}
