package ua.hnatiuk.observerservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hnatiuk Volodymyr on 26.03.2024.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutoData {
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("race")
    private String mileage;
    @JsonProperty("gearboxName")
    private String transmission;
    @JsonProperty("fuelName")
    private String fuelType;
}
