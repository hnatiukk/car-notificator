package ua.hnatiuk.telegramnotificationservice.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.hnatiuk.telegramnotificationservice.exception.EmailNotFoundException;
import ua.hnatiuk.telegramnotificationservice.service.PeopleService;

import java.util.regex.Pattern;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Component
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

        SendMessage message = new SendMessage();
        message.setText(tryAssignChatId(userMessageText, chatId));
        message.setChatId(chatId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
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
                        response = "Ви успішно прив`язали телеграм.";
                    } else response = "Це не email.";
                } catch (EmailNotFoundException e) {
                    response = "Такий email не зареєстровано.";
                }
            }
        } else
            response = "Щоб з`єднати ваш телеграм з CarNotificator, відправте:\n\n/link \"email адреса, на яку реєструвались\"";
        return response;
    }

    public void sendMessageWithPhoto(Long chatId, String text, String photoUrl) {
        SendPhoto message = new SendPhoto();
        message.setPhoto(new InputFile(photoUrl));
        message.setCaption(text);
        message.setChatId(chatId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "AutoRiaNotificatorBot";
    }
}
