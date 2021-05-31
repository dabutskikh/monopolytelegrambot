package ru.dabutsikh.monopolytelegrambot.service.interfaces;

import ru.dabutsikh.monopolytelegrambot.model.Game;
import ru.dabutsikh.monopolytelegrambot.model.Player;

public interface GameService {

    Game findById(Long gameId);

    Game create(Player creator);

    Game start(Player creator, Game game);

    Game finish(Player creator, Game game);

    Game setStartMoney(Player creator, Game game, Integer startMoney);

    Game setForwardMoney(Player creator, Game game, Integer forwardMoney);

    Game setForwardMoneyTime(Player creator, Game game, Integer forwardMoneyTime);
}
