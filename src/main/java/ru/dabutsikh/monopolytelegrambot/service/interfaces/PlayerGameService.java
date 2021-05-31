package ru.dabutsikh.monopolytelegrambot.service.interfaces;

import ru.dabutsikh.monopolytelegrambot.model.Game;
import ru.dabutsikh.monopolytelegrambot.model.Player;
import ru.dabutsikh.monopolytelegrambot.model.PlayerGame;
import ru.dabutsikh.monopolytelegrambot.model.PlayerGameState;

import java.util.List;

public interface PlayerGameService {

    PlayerGame getPlayerGameByGameAndPlayer(Game game, Player player);

    PlayerGame getProcessingGameByPlayer(Player player);

    PlayerGame getCreatedOrProcessingGameByPlayer(Player player);

    PlayerGame getCreatedGameByPlayer(Player player);

    PlayerGame joinGame(Game game, Player player);

    Game startCreatedGame(Player player);

    Game setStartMoneyInCreatedGame(Player player, Integer startMoney);

    Game setForwardMoneyInCreatedGame(Player player, Integer forwardMoney);

    Game setForwardMoneyTimeInCreatedGame(Player player, Integer forwardMoneyTime);

    Game finishGame(Player player);

    List<PlayerGame> getByGame(Game game);

    void quitGame(PlayerGame playerGame);

    void becomeSpectator(PlayerGame playerGame);

    void chargeMoney(PlayerGame playerGame, Integer amount);

    void writeOffMoney(PlayerGame playerGame, Integer amount);

    List<PlayerGame> getActivePlayerGamesByGame(Game game);

    List<PlayerGame> getActiveAndSpectatorPlayerGamesByGame(Game game);

    void setState(PlayerGame playerGame, PlayerGameState state);
}
