package ru.dabutsikh.monopolytelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dabutsikh.monopolytelegrambot.config.GameConfig;
import ru.dabutsikh.monopolytelegrambot.model.Game;
import ru.dabutsikh.monopolytelegrambot.model.GameStatus;
import ru.dabutsikh.monopolytelegrambot.model.Player;
import ru.dabutsikh.monopolytelegrambot.model.PlayerGame;
import ru.dabutsikh.monopolytelegrambot.repository.GameRepository;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.GameService;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.PlayerGameService;

import java.util.Date;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameConfig gameConfig;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerGameService playerGameService;

    @Override
    public Game findById(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(
                () -> new RuntimeException("Игры с id " + gameId + " не существует")
        );
    }

    @Override
    public Game create(Player creator) {
        if (playerGameService.getCreatedOrProcessingGameByPlayer(creator) == null) {
            Game game = new Game();
            game.setStartMoney(gameConfig.getStartMoney());
            game.setForwardMoney(gameConfig.getForwardMoney());
            game.setForwardMoneyTime(gameConfig.getForwartMoneyTime());
            game.setStatus(GameStatus.CREATED);
            game.setCreator(creator);
            return gameRepository.saveAndFlush(game);
        } else {
            throw new RuntimeException("Игрок с id " + creator.getId() + " уже играет");
        }

    }

    @Override
    public void start(Player creator, Game game) {
        if (!game.getCreator().equals(creator)) {
            throw new RuntimeException("Вы не являетесь создателем игры с id " + game.getId());
        }
        if (GameStatus.IN_PROCESS.equals(game.getStatus())) {
            throw new RuntimeException("Игра с id " + game.getId() + " уже началась.");
        }
        if (GameStatus.FINISHED.equals(game.getStatus())) {
            throw new RuntimeException("Игра с id " + game.getId() + " уже окончена.");
        }
        List<PlayerGame> playerGames = playerGameService.getByGame(game);
        long activePlayersCount = playerGames.stream().map(PlayerGame::getStatus).count();
        if (activePlayersCount < 2) {
            throw new RuntimeException("Для старта игры с id " + game.getId() + " не хватает активных игроков.");
        }
        List<PlayerGame> activePlayerGames = playerGameService.getActivePlayerGamesByGame(game);
        activePlayerGames.forEach(
                playerGame -> playerGameService.chargeMoney(playerGame, game.getStartMoney())
        );
        game.setStatus(GameStatus.IN_PROCESS);
        game.setDateBegin(new Date());
        gameRepository.saveAndFlush(game);
    }

    @Override
    // todo: сделать сохранение победителя
    public void finish(Player creator, Game game) {
        if (!game.getCreator().equals(creator)) {
            throw new RuntimeException("Вы не являетесь создателем игры с id " + game.getId());
        }
        if (GameStatus.FINISHED.equals(game.getStatus())) {
            throw new RuntimeException("Игра с id " + game.getId() + " уже закончена");
        }
        game.setStatus(GameStatus.FINISHED);
        game.setDateEnd(new Date());
        gameRepository.saveAndFlush(game);
    }

    @Override
    public void setStartMoney(Player creator, Game game, Integer startMoney) {
        if (!game.getCreator().equals(creator)) {
            throw new RuntimeException("Вы не являетесь создателем игры с id " + game.getId());
        }
        if (!GameStatus.CREATED.equals(game.getStatus())) {
            throw new RuntimeException("Игра уже начата");
        }
        game.setStartMoney(startMoney);
        gameRepository.saveAndFlush(game);
    }

    @Override
    public void setForwardMoney(Player creator, Game game, Integer forwardMoney) {
        if (!game.getCreator().equals(creator)) {
            throw new RuntimeException("Вы не являетесь создателем игры с id " + game.getId());
        }
        if (!GameStatus.CREATED.equals(game.getStatus())) {
            throw new RuntimeException("Игра уже начата");
        }
        game.setStartMoney(forwardMoney);
        gameRepository.saveAndFlush(game);
    }

    @Override
    public void setForwardMoneyTime(Player creator, Game game, Integer forwardMoneyTime) {
        if (!game.getCreator().equals(creator)) {
            throw new RuntimeException("Вы не являетесь создателем игры с id " + game.getId());
        }
        if (!GameStatus.CREATED.equals(game.getStatus())) {
            throw new RuntimeException("Игра уже начата");
        }
        game.setStartMoney(forwardMoneyTime);
        gameRepository.saveAndFlush(game);
    }
}
