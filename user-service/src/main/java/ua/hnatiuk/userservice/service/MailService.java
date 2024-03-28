package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

import static java.lang.StringTemplate.STR;

/**
 * @author Hnatiuk Volodymyr on 28.03.2024.
 */
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    public int sendVerificationCode(String toAddress) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject("Підтвердження пошти");

        int code = new Random().nextInt(100_000, 1_000_000);

        simpleMailMessage.setText(STR."Код для перевірки: \{code}");
        emailSender.send(simpleMailMessage);

        return code;
    }
}