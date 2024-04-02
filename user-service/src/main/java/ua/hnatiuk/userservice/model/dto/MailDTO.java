package ua.hnatiuk.userservice.model.dto;

import lombok.*;

/**
 * @author Hnatiuk Volodymyr on 02.04.2024.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailDTO {
    private String toAddress;
    private String subject;
    private String text;
}
