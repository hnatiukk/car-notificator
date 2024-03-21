package org.hnatiuk.carnotificator.model.entity;

import jakarta.persistence.*;
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
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "tg_chat_id")
    private String tgChatId;
    @Column(name = "role")
    private String role;
    @OneToMany(mappedBy = "owner")
    private List<Subscription> subscriptions;
    public Person(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
