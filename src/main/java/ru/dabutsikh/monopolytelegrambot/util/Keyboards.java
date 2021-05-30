package ru.dabutsikh.monopolytelegrambot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.dabutsikh.monopolytelegrambot.model.Player;
import ru.dabutsikh.monopolytelegrambot.model.PlayerGame;
import ru.dabutsikh.monopolytelegrambot.model.PlayerGameStatus;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.GameService;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.PlayerGameService;

import java.util.List;

@Component
public class Keyboards {

    @Autowired
    PlayerGameService playerGameService;

    @Autowired
    GameService gameService;

    public ReplyKeyboard getKeyboard(Player player) {
        PlayerGame playerGame = playerGameService.getProcessingGameByPlayer(player);
        if (playerGame != null && PlayerGameStatus.ACTIVE.equals(playerGame.getStatus())) {
            switch (playerGame.getState()) {
                case INITIALIZE:
                    return new ReplyKeyboardRemove(true);

                case DEFAULT:
                    KeyboardRow first = new KeyboardRow();
                    first.addAll(List.of("Перевод игроку", "Баланс", "Получить из банка"));

                    KeyboardRow second = new KeyboardRow();
                    second.addAll(List.of("Перевод банку", "Пройти поле ВПЕРЕД", "AufWiedersehen"));

                    return new ReplyKeyboardMarkup(List.of(first, second), true, true, true);

                case CONFIRM_TO_BANK:
                    KeyboardRow row = new KeyboardRow();
                    row.addAll(List.of("ОК", "Отмена"));
                    return new ReplyKeyboardMarkup(List.of(row), true, true, true);

                case CONFIRM_TRANSFER:
                    row = new KeyboardRow();
                    row.addAll(List.of("ОК", "Отмена"));
                    return new ReplyKeyboardMarkup(List.of(row), true, true, true);

                case CONFIRM_FROM_BANK:
                    row = new KeyboardRow();
                    row.addAll(List.of("ОК", "Отмена"));
                    return new ReplyKeyboardMarkup(List.of(row), true, true, true);

                case SELECT_RECIPIENT:
                    // todo: Клавиатура с активными игроками помимо player
                    return new ReplyKeyboardRemove(true);

                case SELECT_MONEY_TO_BANK:
                    return new ReplyKeyboardRemove(true);

                case SELECT_MONEY_TRANSFER:
                    return new ReplyKeyboardRemove(true);

                case SELECT_MONEY_FROM_BANK:
                    return new ReplyKeyboardRemove(true);
            }

            return new ReplyKeyboardRemove(true);

        } else {
            return new ReplyKeyboardRemove(true);
        }
    }
}
