package ua.hnatiuk.notificationservice.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.hnatiuk.notificationservice.service.PeopleService;


/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Component
@Slf4j
public class TelegramNotificationBot extends TelegramLongPollingBot {
    private final PeopleService peopleService;

    @Autowired
    public TelegramNotificationBot(@Value("${telegram.apikey}") String botToken,
                                   PeopleService peopleService) {
        super(botToken);
        this.peopleService = peopleService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().isUserMessage() || !update.getMessage().hasText() || update.getMessage().getText().isEmpty())
            return;

        Long chatId = update.getMessage().getChatId();
        String userMessageText = update.getMessage().getText();

        log.debug("Received message {} from {}", userMessageText, chatId);

        SendMessage message = new SendMessage();
        message.setText(peopleService.tryAssignChatId(userMessageText, chatId));
        message.setChatId(chatId);

        try {
            execute(message);
            log.info("Replied message to {}", chatId);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException appeared");
            throw new RuntimeException(e);
        }
    }

    public void sendMessageWithPhoto(Long chatId, String text, String photoUrl) {
        SendPhoto message = new SendPhoto();
        message.setPhoto(new InputFile(photoUrl));
        message.setCaption(text);
        message.setChatId(chatId);

        try {
            execute(message);
            log.info("Sent message with photo to {}", chatId);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException appeared");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "AutoRiaNotificatorBot";
    }
}
