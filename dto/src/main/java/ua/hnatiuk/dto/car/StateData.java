package ua.hnatiuk.dto.car;

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
public class StateData {
    @JsonProperty("name")
    private String city;
    @JsonProperty("regionName")
    private String region;
}
