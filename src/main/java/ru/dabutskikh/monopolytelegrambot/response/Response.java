package ru.dabutskikh.monopolytelegrambot.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response {

    Long telegramId;
    String message;
    ReplyKeyboard keyboard;

    public Response(Long telegramId, String message) {
        this.telegramId = telegramId;
        this.message = message;
    }
}
