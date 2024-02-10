package ru.dabutskikh.monopolytelegrambot;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.dabutskikh.monopolytelegrambot.config.bot.BotConfig;

import javax.annotation.PostConstruct;

@Component
public class Bot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    public Bot(BotConfig botConfig) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
    }

    @PostConstruct
    @SneakyThrows
    public void registerBot() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("onUpdateReceived");
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }
}
