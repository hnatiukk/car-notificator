package ua.hnatiuk.userservice.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.hnatiuk.userservice.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnatiuk.enums.FuelType;
import ua.hnatiuk.enums.TransmissionType;

import java.util.List;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByIsActiveTrue();
    @Modifying
    @Query("UPDATE Subscription s SET s.brand = :brand, s.model = :model, s.priceStart = :priceStart, s.priceEnd = :priceEnd, s.yearStart = :yearStart, s.yearEnd = :yearEnd, s.mileageStart = :mileageStart, s.mileageEnd = :mileageEnd, s.transmissionType = :transmissionType, s.fuelType = :fuelType, s.brandId = :brandId, s.modelId = :modelId WHERE s.id = :id")
    void updateSubscription(@Param("id") Long id,
                            @Param("brand") String brand,
                            @Param("model") String model,
                            @Param("priceStart") Integer priceStart,
                            @Param("priceEnd") Integer priceEnd,
                            @Param("yearStart") Integer yearStart,
                            @Param("yearEnd") Integer yearEnd,
                            @Param("mileageStart") Integer mileageStart,
                            @Param("mileageEnd") Integer mileageEnd,
                            @Param("transmissionType") TransmissionType transmissionType,
                            @Param("fuelType") FuelType fuelType,
                            @Param("brandId") Integer brandId,
                            @Param("modelId") Integer modelId);
}
