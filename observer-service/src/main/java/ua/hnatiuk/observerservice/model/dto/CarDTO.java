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
public class CarDTO {
    @JsonProperty("markName")
    private String brand;
    @JsonProperty("modelName")
    private String model;
    @JsonProperty("USD")
    private Integer price;
    @JsonProperty("autoData")
    private AutoData autoData;
    @JsonProperty("stateData")
    private StateData stateData;
    @JsonProperty("linkToView")
    private String link;
}