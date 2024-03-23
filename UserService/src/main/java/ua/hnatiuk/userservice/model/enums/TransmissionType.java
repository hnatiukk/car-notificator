package ua.hnatiuk.userservice.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Getter
@RequiredArgsConstructor
public enum TransmissionType {
    MANUAL("Ручна / Механіка"),
    AUTOMATIC("Автомат"),
    TIPTRONIC("Типтронік"),
    ROBOT("Робот"),
    VARIATOR("Варіатор");
    private final String name;

}
