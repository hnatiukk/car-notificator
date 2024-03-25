package ua.hnatiuk.userservice.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Hnatiuk Volodymyr on 21.03.2024.
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
    @NotEmpty(message = "Емейл не може бути пустим")
    @Email(message = "Це не емейл")
    private String email;
    @Column(name = "password")
    @NotEmpty(message = "Пароль не може бути пустим")
    @Size(min = 6, message = "Пароль повинен бути довше 6 символів")
    private String password;
    @Column(name = "tg_chat_id")
    private String tgChatId;
    @Column(name = "role")
    private String role;
    @OneToMany(mappedBy = "owner")
    private List<Subscription> subscriptions;
}
