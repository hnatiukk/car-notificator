package ua.hnatiuk.userservice.utils;

import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.model.entity.Subscription;
import ua.hnatiuk.userservice.model.enums.FuelType;
import ua.hnatiuk.userservice.model.enums.TransmissionType;

/**
 * @author Hnatiuk Volodymyr on 03.04.2024.
 */
public class DataUtils {
    public static Person getPersonTransient() {
        return Person.builder()
                .email("test@gmail.com")
                .password("password")
                .role("ROLE_USER")
                .tgChatId(4392041L)
                .build();
    }

    public static Person getPersonPersisted() {
        return Person.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("password")
                .role("ROLE_USER")
                .tgChatId(4392041L)
                .build();
    }

    public static Subscription getSubscriptionTransient() {
        return Subscription.builder()
                .brand("BMW")
                .model("X5")
                .priceStart(10_000)
                .priceEnd(30_000)
                .yearStart(2010)
                .yearEnd(2020)
                .mileageStart(1)
                .mileageEnd(200)
                .transmissionType(TransmissionType.AUTOMATIC)
                .fuelType(FuelType.GASOLINE)
                .isActive(true)
                .brandId(9)
                .modelId(96)
                .build();
    }
}
