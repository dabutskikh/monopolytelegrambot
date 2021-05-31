package ru.dabutsikh.monopolytelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dabutsikh.monopolytelegrambot.model.*;
import ru.dabutsikh.monopolytelegrambot.repository.PlayerGameRepository;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.GameService;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.PlayerGameService;

import java.util.List;

@Service
public class PlayerGameServiceImpl implements PlayerGameService {

    @Autowired
    PlayerGameRepository playerGameRepository;

    @Autowired
    GameService gameService;

    @Override
    public PlayerGame getProcessingGameByPlayer(Player player) {
        List<PlayerGame> result = playerGameRepository.getProcessingGameByPlayerId(player.getId());
        return result.size() != 0 ? result.get(0) : null;
    }

    @Override
    public PlayerGame getCreatedOrProcessingGameByPlayer(Player player) {
        List<PlayerGame> result = playerGameRepository.getCreatedOrProcessingGameByPlayerId(player.getId());
        return result.size() != 0 ? result.get(0) : null;
    }

    @Override
    public PlayerGame getCreatedGameByPlayer(Player player) {
        List<PlayerGame> result = playerGameRepository.getCreatedGameByPlayerId(player.getId());
        return result.size() != 0 ? result.get(0) : null;
    }

    @Override
    public PlayerGame getPlayerGameByGameAndPlayer(Game game, Player player) {
        List<PlayerGame> result = playerGameRepository.getPlayerGameByGameIdAndPlayerId(game.getId(), player.getId());
        return result.size() != 0 ? result.get(0) : null;
    }

    @Override
    public PlayerGame joinGame(Game game, Player player) {
        PlayerGame playerGame = this.getProcessingGameByPlayer(player);
        if (playerGame != null) {
            if (GameStatus.FINISHED.equals(playerGame.getId().getGame().getStatus())) {
                throw new RuntimeException("Игра с id " + playerGame.getId().getGame().getId() + " уже закончена.");
            }
            throw new RuntimeException("Вы уже играете в игре с id " + playerGame.getId().getGame().getId() + ".");
        }
        PlayerGame targetPlayerGame = this.getPlayerGameByGameAndPlayer(game, player);
        if (targetPlayerGame == null) {
            switch (game.getStatus()) {
                case CREATED:
                    PlayerGame newPlayerGame = new PlayerGame(player, game);
                    newPlayerGame.setStatus(PlayerGameStatus.ACTIVE);
                    newPlayerGame.setState(PlayerGameState.INITIALIZE);
                    return playerGameRepository.saveAndFlush(newPlayerGame);
                case IN_PROCESS:
                    newPlayerGame = new PlayerGame(player, game);
                    newPlayerGame.setStatus(PlayerGameStatus.SPECTATOR);
                    return playerGameRepository.saveAndFlush(newPlayerGame);
                case FINISHED:
                    throw new RuntimeException("Игра с id " + game.getId() + " уже закончена.");
            }
            return null;
        } else {
            if (!targetPlayerGame.getStatus().equals(PlayerGameStatus.DISCONNECTED)) {
                if (GameStatus.FINISHED.equals(game.getStatus())) {
                    throw new RuntimeException("Игра с id " + game.getId() + " уже закончена.");
                }
                throw new RuntimeException("Вы уже есть в игре c id " + game.getId() + ".");
            } else {
                targetPlayerGame.setStatus(PlayerGameStatus.SPECTATOR);
                return playerGameRepository.saveAndFlush(targetPlayerGame);
            }
        }
    }

    @Override
    public Game startCreatedGame(Player player) {
        PlayerGame playerGame = getCreatedGameByPlayer(player);
        if (playerGame == null) {
            throw new RuntimeException("Вы не состоите ни в одной игре, находящейся на стадии создания");
        }
        Game game = playerGame.getId().getGame();
        return gameService.start(player, game);
    }

    @Override
    public Game setStartMoneyInCreatedGame(Player player, Integer startMoney) {
        PlayerGame playerGame = getCreatedGameByPlayer(player);
        if (playerGame == null) {
            throw new RuntimeException("Вы не состоите ни в одной игре, находящейся на стадии создания");
        }
        Game game = playerGame.getId().getGame();
        return gameService.setStartMoney(player, game, startMoney);
    }

    @Override
    public Game setForwardMoneyInCreatedGame(Player player, Integer forwardMoney) {
        PlayerGame playerGame = getCreatedGameByPlayer(player);
        if (playerGame == null) {
            throw new RuntimeException("Вы не состоите ни в одной игре, находящейся на стадии создания");
        }
        Game game = playerGame.getId().getGame();
        return gameService.setForwardMoney(player, game, forwardMoney);
    }

    @Override
    public Game setForwardMoneyTimeInCreatedGame(Player player, Integer forwardMoneyTime) {
        PlayerGame playerGame = getCreatedGameByPlayer(player);
        if (playerGame == null) {
            throw new RuntimeException("Вы не состоите ни в одной игре, находящейся на стадии создания");
        }
        Game game = playerGame.getId().getGame();
        return gameService.setForwardMoneyTime(player, game, forwardMoneyTime);
    }

    @Override
    public Game finishGame(Player player) {
        PlayerGame playerGame = getCreatedOrProcessingGameByPlayer(player);
        if (playerGame == null) {
            throw new RuntimeException("Вы не состоите ни в одной незавершенной игре");
        }
        Game game = playerGame.getId().getGame();
        return gameService.finish(player, game);
    }

    @Override
    public List<PlayerGame> getByGame(Game game) {
        return playerGameRepository.getByGameId(game.getId());
    }

    @Override
    public void quitGame(PlayerGame playerGame) {
        if (playerGame == null) {
            throw new RuntimeException("Вы не состоите ни в одной активной игре");
        }
        if (!playerGame.getStatus().equals(PlayerGameStatus.DISCONNECTED)) {
            playerGame.setStatus(PlayerGameStatus.DISCONNECTED);
            playerGameRepository.saveAndFlush(playerGame);
        } else {
            throw new RuntimeException("Вы уже вышли из игры с id " + playerGame.getId().getGame().getId() + ".");
        }
    }

    @Override
    public void becomeSpectator(PlayerGame playerGame) {
        if (!playerGame.getStatus().equals(PlayerGameStatus.SPECTATOR)) {
            playerGame.setStatus(PlayerGameStatus.SPECTATOR);
            playerGameRepository.saveAndFlush(playerGame);
        } else {
            throw new RuntimeException("Вы уже являетесь наблюдателем в игре с id " + playerGame.getId().getGame().getId() + ".");
        }
    }

    @Override
    public void chargeMoney(PlayerGame playerGame, Integer amount) {
        Integer currentMoney = playerGame.getCurrentMoney();
        playerGame.setCurrentMoney(currentMoney + amount);
        playerGameRepository.saveAndFlush(playerGame);
    }

    @Override
    public void writeOffMoney(PlayerGame playerGame, Integer amount) {
        Integer currentMoney = playerGame.getCurrentMoney();
        if (currentMoney - amount < 0) {
            throw new RuntimeException("Недостаточно денег для перевода. Не хватает " + (amount - currentMoney));
        }
        playerGame.setCurrentMoney(currentMoney - amount);
        playerGameRepository.saveAndFlush(playerGame);
    }

    @Override
    public List<PlayerGame> getActivePlayerGamesByGame(Game game) {
        return playerGameRepository.getActivePlayerGamesByGameId(game.getId());
    }

    @Override
    public List<PlayerGame> getActiveAndSpectatorPlayerGamesByGame(Game game) {
        return playerGameRepository.getActiveAndSpectatorPlayerGamesByGameId(game.getId());
    }

    @Override
    public void setState(PlayerGame playerGame, PlayerGameState state) {
        playerGame.setState(state);
        playerGameRepository.saveAndFlush(playerGame);
    }
}
