package ua.hnatiuk.dto;

import lombok.*;

/**
 * @author Hnatiuk Volodymyr on 04.04.2024.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private String email;
    private Long tgChatId;
}
