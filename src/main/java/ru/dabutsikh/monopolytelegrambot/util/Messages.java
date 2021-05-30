package ru.dabutsikh.monopolytelegrambot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dabutsikh.monopolytelegrambot.Bot;
import ru.dabutsikh.monopolytelegrambot.model.Player;

@Component
public class Messages {

    @Autowired
    private Keyboards keyboards;

    @Autowired
    private Bot bot;

    public void sendMessage(Player player, String text) throws TelegramApiException {
        bot.execute(
                SendMessage
                        .builder()
                        .chatId(String.valueOf(player.getId()))
                        .text(text)
                        .replyMarkup(keyboards.getKeyboard(player))
                        .build()
        );
    }
}
