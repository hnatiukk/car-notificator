package ua.hnatiuk.userservice.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private Long timestamp;
}
