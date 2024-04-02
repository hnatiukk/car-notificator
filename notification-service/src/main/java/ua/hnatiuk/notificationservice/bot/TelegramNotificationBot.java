package ua.hnatiuk.notificationservice.bot;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.hnatiuk.notificationservice.exception.EmailNotFoundException;
import ua.hnatiuk.notificationservice.model.dto.MessageDTO;
import ua.hnatiuk.notificationservice.service.PeopleService;
import org.telegram.telegrambots.meta.generics.BotSession;

import java.util.regex.Pattern;


/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Component
@Slf4j
public class TelegramNotificationBot extends TelegramLongPollingBot {
    private final PeopleService peopleService;
    private BotSession botSession;

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
        message.setText(tryAssignChatId(userMessageText, chatId));
        message.setChatId(chatId);

        try {
            execute(message);
            log.info("Replied for {}", chatId);
        } catch (TelegramApiException e) {
            log.error("TelegramApiException appeared");
            throw new RuntimeException(e);
        }
    }
    private String tryAssignChatId(String userMessageText, Long chatId) {
        String response;

        if (userMessageText.startsWith("/link")) {
            String[] split = userMessageText.split(" ");
            if (split.length != 2) {
                response = "Введіть у форматі:\n\n/link \"email адреса, на яку реєструвались\"";
            } else {
                try {
                    String email = split[1];
                    if (Pattern.matches("^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", email)) {
                        peopleService.assignChatId(email, chatId);
                        response = "Ви успішно прив'язали телеграм.";
                    } else response = "Це не email.";
                } catch (EmailNotFoundException e) {
                    log.warn("This email is not registered");
                    response = "Такий email не зареєстровано.";
                }
            }
        } else
            response = "Щоб з'єднати ваш телеграм з CarNotificator, відправте:\n\n/link \"email адреса, на яку реєструвались\"";
        return response;
    }

    public void sendMessageWithPhoto(MessageDTO messageDTO) {
        SendPhoto message = new SendPhoto();
        message.setPhoto(new InputFile(messageDTO.getPhotoUrl()));
        message.setCaption(messageDTO.getText());
        message.setChatId(messageDTO.getChatId());

        try {
            execute(message);
            log.info("Sent message with photo to {}", messageDTO.getChatId());
        } catch (TelegramApiException e) {
            log.error("TelegramApiException appeared");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "AutoRiaNotificatorBot";
    }

    @PostConstruct
    private void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botSession = botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error("Could not register bot instance");
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    private void stopBotSession() {
        botSession.stop();
        log.debug("Stopped bot session");
    }
}
