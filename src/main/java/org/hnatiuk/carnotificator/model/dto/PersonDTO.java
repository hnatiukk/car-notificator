package org.hnatiuk.carnotificator.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    @NotEmpty(message = "Емейл не може бути пустим")
    @Email(message = "Це не емейл")
    private String email;
    @NotEmpty(message = "Пароль не може бути пустим")
    @Size(min = 6, message = "Пароль повинен бути довше 6 символів")
    private String password;
}
