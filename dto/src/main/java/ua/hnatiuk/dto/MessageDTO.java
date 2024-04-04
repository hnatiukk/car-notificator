package ua.hnatiuk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hnatiuk Volodymyr on 01.04.2024.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long chatId;
    private String text;
    private String photoUrl;
}
