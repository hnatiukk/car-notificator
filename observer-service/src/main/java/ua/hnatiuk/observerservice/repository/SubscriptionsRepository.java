package ua.hnatiuk.observerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnatiuk.observerservice.model.entity.Subscription;

import java.util.List;

/**
 * @author Hnatiuk Volodymyr on 26.03.2024.
 */
public interface SubscriptionsRepository extends JpaRepository<Subscription, Long>{
    List<Subscription> findAllByIsActiveTrue();
}
