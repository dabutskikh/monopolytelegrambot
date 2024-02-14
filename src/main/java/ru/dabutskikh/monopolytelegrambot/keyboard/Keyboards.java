package ru.dabutskikh.monopolytelegrambot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Keyboards {

    public static ReplyKeyboard getDefault() {
        KeyboardRow first = new KeyboardRow();
        first.addAll(List.of("Перевод игроку", "Заплатить банку", "Получить из банка"));

        KeyboardRow second = new KeyboardRow();
        second.addAll(List.of("Пройти поле ВПЕРЕД", "Баланс", "Auf Wiedersehen"));
        return new ReplyKeyboardMarkup(List.of(first, second), true, true, false, "Выберите действие", true);
    }

    public static ReplyKeyboard getWithRows(List<String> rows) {
        List<KeyboardRow> keyboardRows = rows.stream()
                .map(row -> Collections.singletonList(KeyboardButton.builder().text(row).build()))
                .map(KeyboardRow::new)
                .collect(Collectors.toList());
        return new ReplyKeyboardMarkup(keyboardRows, true, true, false, "Выберите игрока", true);
    }

    public static ReplyKeyboard getConfirmKeyboard() {
        return new ReplyKeyboardMarkup(
                Collections.singletonList(new KeyboardRow(List.of(
                        new KeyboardButton("OK"),
                        new KeyboardButton("Отмена")
                ))),
                true, true, false, "Подтвердите операцию", true
        );
    }

    public static ReplyKeyboard remove() {
        return new ReplyKeyboardRemove(true);
    }
}
