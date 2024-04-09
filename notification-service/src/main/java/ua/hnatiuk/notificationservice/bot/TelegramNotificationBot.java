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
import ua.hnatiuk.dto.MessageDTO;
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
    private static final String LINK_FORMAT_MESSAGE = "Введіть у форматі:\n\n/link \"email адреса, на яку реєструвались\"";
    private static final String SUCCESS_MESSAGE = "Ви успішно прив'язали телеграм.";
    private static final String INVALID_EMAIL_MESSAGE = "Це не email.";
    private static final String EMAIL_NOT_FOUND_MESSAGE = "Такий email не зареєстровано.";
    private static final String INSTRUCTION_MESSAGE = "Щоб з'єднати ваш телеграм з CarNotificator, відправте:\n\n/link \"email адреса, на яку реєструвались\"";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$");


    @Autowired
    public TelegramNotificationBot(@Value("${telegram.apikey}") String botToken,
                                   PeopleService peopleService) {
        super(botToken);
        this.peopleService = peopleService;
    }

    /**
     * Precesses incoming messages from users
     * @param update Update object from user
     */
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

    /**
     * Tries ti assign chat id and gets response for user
     * @param userMessageText Text from user message
     * @param chatId Chat id of user
     * @return Response for user
     */
    public String tryAssignChatId(String userMessageText, Long chatId) {
        String response;

        if (userMessageText.startsWith("/link")) {
            String[] split = userMessageText.split(" ");
            if (split.length != 2) {
                response = LINK_FORMAT_MESSAGE;
            } else {
                try {
                    String email = split[1];
                    if (EMAIL_PATTERN.matcher(email).matches()) {
                        peopleService.assignChatId(email, chatId);
                        response = SUCCESS_MESSAGE;
                    } else response = INVALID_EMAIL_MESSAGE;
                } catch (EmailNotFoundException e) {
                    log.warn("This email is not registered");
                    response = EMAIL_NOT_FOUND_MESSAGE;
                }
            }
        } else
            response = INSTRUCTION_MESSAGE;
        return response;
    }

    /**
     * Sends message with photo to user
     * @param messageDTO MessageDTO
     */
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

    /**
     * Method needed by library
     * @return Bot username
     */
    @Override
    public String getBotUsername() {
        return "AutoRiaNotificatorBot";
    }

    /**
     * Registers bot on a bean creation
     */
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

    /**
     * Stops bot session on a bean destroying
     */
    @PreDestroy
    private void stopBotSession() {
        botSession.stop();
        log.debug("Stopped bot session");
    }
}
