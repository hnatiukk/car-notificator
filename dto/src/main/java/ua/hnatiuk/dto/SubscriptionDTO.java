package ua.hnatiuk.dto;

import lombok.*;
import ua.hnatiuk.enums.FuelType;
import ua.hnatiuk.enums.TransmissionType;

/**
 * @author Hnatiuk Volodymyr on 04.04.2024.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {
    private String brand;
    private String model;
    private Integer priceStart;
    private Integer priceEnd;
    private Integer yearStart;
    private Integer yearEnd;
    private Integer mileageStart;
    private Integer mileageEnd;
    private TransmissionType transmissionType;
    private FuelType fuelType;
    private Boolean isActive;
    private Integer brandId;
    private Integer modelId;
    private PersonDTO owner;
}
