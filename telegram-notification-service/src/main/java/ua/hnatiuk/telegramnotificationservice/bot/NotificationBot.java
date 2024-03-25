package ua.hnatiuk.telegramnotificationservice.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.hnatiuk.telegramnotificationservice.exception.EmailNotFoundException;
import ua.hnatiuk.telegramnotificationservice.service.PeopleService;

/**
 * @author Hnatiuk Volodymyr on 25.03.2024.
 */
@Component
public class NotificationBot extends TelegramLongPollingBot {
    private final PeopleService peopleService;
    @Autowired
    public NotificationBot(@Value("${telegram.apikey}") String botToken,
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

        String response;

        if (userMessageText.startsWith("/link")) {
            String[] split = userMessageText.split(" ");
            if (split.length != 2) {
                response = "Введіть у форматі:\n/link (Email адреса, на яку реєструвались).";
            }
            else {
                String email = split[1];
                try {
                    peopleService.assignChatId(email, chatId);
                    response = "Ви успішно прив`язали телеграм.";
                } catch (EmailNotFoundException e) {
                    response = "Такий email не зареєстровано.";
                }
            }
        }
        else response = "Щоб з`єднати ваш телеграм з CarNotificator, відправте:\n/link (Email адреса, на яку реєструвались).";

        SendMessage message = new SendMessage();
        message.setText(response);
        message.setChatId(chatId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMessage(Long chatId, String text) {

    }
    @Override
    public String getBotUsername() {
        return "AutoRiaNotificatorBot";
    }

    @PostConstruct
    private void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            System.err.println("Не вдалось зареєструвати бота");
        }
    }
}
