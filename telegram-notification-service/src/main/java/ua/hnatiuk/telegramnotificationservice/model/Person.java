package ua.hnatiuk.telegramnotificationservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    @Email(message = "Це не емейл")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "tg_chat_id")
    private Long tgChatId;
    @Column(name = "role")
    private String role;
}