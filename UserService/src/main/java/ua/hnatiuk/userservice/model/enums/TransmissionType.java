package ua.hnatiuk.userservice.model.enums;

import lombok.RequiredArgsConstructor;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@RequiredArgsConstructor
public enum TransmissionType {
    MANUAL("Ручна / Механіка"),
    AUTOMATIC("Автомат"),
    TIPTRONIC("Типтронік"),
    ROBOT("Робот"),
    VARIATOR("Варіатор");
    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
