package ru.dabutsikh.monopolytelegrambot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component("Bot")
@NoArgsConstructor
@Getter
@PropertySource("classpath:application.properties")
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        execute(SendMessage
                .builder()
                .chatId(Long.toString(update.getMessage().getChatId()))
                .text("Hello")
                .build());
    }
}
