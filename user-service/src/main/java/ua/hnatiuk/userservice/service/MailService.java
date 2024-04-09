package ua.hnatiuk.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ua.hnatiuk.dto.MailDTO;

import java.util.Random;

import static java.lang.StringTemplate.STR;

/**
 * @author Hnatiuk Volodymyr on 28.03.2024.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final KafkaTemplate<String, MailDTO> kafkaTemplate;

    /**
     * Sends verification code and specified address to kafka
     * @param toAddress Email address to send code
     * @return Sent code
     */
    public int sendVerificationCode(String toAddress) {
        int code = new Random().nextInt(100_000, 1_000_000);

        MailDTO mailDTO = MailDTO.builder()
                .toAddress(toAddress)
                .subject("Підтвердження пошти")
                .text(STR."Ваш код підтвердження: \{code}")
                .build();

        kafkaTemplate.send("carnotificator.mail", mailDTO);
        log.info("Sent code {} to {}", code, toAddress);

        return code;
    }
}