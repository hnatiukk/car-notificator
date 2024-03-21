package org.hnatiuk.carnotificator.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hnatiuk.carnotificator.model.enums.FuelType;
import org.hnatiuk.carnotificator.model.enums.TransmissionType;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "brand")
    private String brand;
    @Column(name = "model")
    private String model;
    @Column(name = "price_start")
    private Integer priceStart;
    @Column(name = "price_end")
    private Integer priceEnd;
    @Column(name = "year_start")
    private Integer yearStart;
    @Column(name = "year_end")
    private Integer yearEnd;
    @Column(name = "mileage_start")
    private Integer mileageStart;
    @Column(name = "mileage_end")
    private Integer mileageEnd;
    @Column(name = "transmission")
    @Enumerated(EnumType.ORDINAL)
    private TransmissionType transmissionType;
    @Column(name = "fuel_type")
    @Enumerated(EnumType.ORDINAL)
    private FuelType fuelType;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "request_url")
    private String requestUrl;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id");
    private Person owner;
}
