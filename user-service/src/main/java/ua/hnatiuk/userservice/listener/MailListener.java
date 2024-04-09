package ua.hnatiuk.userservice.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ua.hnatiuk.dto.MailDTO;


/**
 * @author Hnatiuk Volodymyr on 02.04.2024.
 */
@Component
@RequiredArgsConstructor
public class MailListener {
    private final JavaMailSender emailSender;

    /**
     * Consumes mails to send from kafka
     * @param mailDTO DTO of mail message
     */
    @KafkaListener(topics = "carnotificator.mail", groupId = "main", containerFactory = "factory")
    public void sendEmail(MailDTO mailDTO) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mailDTO.getToAddress());
        simpleMailMessage.setSubject(mailDTO.getSubject());
        simpleMailMessage.setText(mailDTO.getText());

        emailSender.send(simpleMailMessage);
    }
}
