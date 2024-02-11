package ru.dabutskikh.monopolytelegrambot;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.dabutskikh.monopolytelegrambot.command.CommandContext;
import ru.dabutskikh.monopolytelegrambot.command.CommandParser;
import ru.dabutskikh.monopolytelegrambot.config.bot.BotConfig;

import javax.annotation.PostConstruct;

@Component
public class Bot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CommandParser commandParser;

    public Bot(BotConfig botConfig, CommandParser commandParser) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        this.commandParser = commandParser;
    }

    @PostConstruct
    @SneakyThrows
    public void registerBot() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        User user = message.getFrom();
        CommandContext context = CommandContext.builder()
                .userId(user.getId())
                .username(user.getUserName())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .text(message.getText())
                .build();
        boolean parse = commandParser.parse(context);
        System.out.println(parse);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }
}
