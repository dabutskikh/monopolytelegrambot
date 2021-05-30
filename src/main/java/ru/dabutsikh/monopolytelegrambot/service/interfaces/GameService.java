package ru.dabutsikh.monopolytelegrambot.service.interfaces;

import ru.dabutsikh.monopolytelegrambot.model.Game;
import ru.dabutsikh.monopolytelegrambot.model.Player;

public interface GameService {

    Game findById(Long gameId);

    Game create(Player creator);

    void start(Player creator, Game game);

    void finish(Player creator, Game game);

    void setStartMoney(Player creator, Game game, Integer startMoney);

    void setForwardMoney(Player creator, Game game, Integer forwardMoney);

    void setForwardMoneyTime(Player creator, Game game, Integer forwardMoneyTime);
}
