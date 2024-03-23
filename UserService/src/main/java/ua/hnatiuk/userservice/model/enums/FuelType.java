package ua.hnatiuk.userservice.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Getter
@RequiredArgsConstructor
public enum FuelType {
    GASOLINE("Бензин"),
    DIESEL("Дизель"),
    GAS("Газ"),
    GAS_GASOLINE("Газ / бензин"),
    HYBRID("Гібрид"),
    ELECTRICAL("Електро");
    private final String name;

}
