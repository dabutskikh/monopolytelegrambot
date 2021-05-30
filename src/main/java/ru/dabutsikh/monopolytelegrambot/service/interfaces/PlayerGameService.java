package ru.dabutsikh.monopolytelegrambot.service.interfaces;

import ru.dabutsikh.monopolytelegrambot.model.Game;
import ru.dabutsikh.monopolytelegrambot.model.Player;
import ru.dabutsikh.monopolytelegrambot.model.PlayerGame;

import java.util.List;

public interface PlayerGameService {

    PlayerGame getPlayerGameByGameAndPlayer(Game game, Player player);

    PlayerGame getProcessingGameByPlayer(Player player);

    PlayerGame getCreatedOrProcessingGameByPlayer(Player player);

    PlayerGame getCreatedGameByPlayer(Player player);

    PlayerGame joinGame(Game game, Player player);

    void setStartMoneyInCreatedGame(Player player, Integer startMoney);

    void setForwardMoneyInCreatedGame(Player player, Integer forwardMoney);

    void setForwardMoneyTimeInCreatedGame(Player player, Integer forwardMoneyTime);

    void finishGame(Player player);

    List<PlayerGame> getByGame(Game game);

    void quitGame(PlayerGame playerGame);

    void becomeSpectator(PlayerGame playerGame);

    void chargeMoney(PlayerGame playerGame, Integer amount);

    void writeOffMoney(PlayerGame playerGame, Integer amount);

    List<PlayerGame> getActivePlayerGamesByGame(Game game);
}
