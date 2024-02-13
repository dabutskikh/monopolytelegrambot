package ru.dabutskikh.monopolytelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dabutskikh.monopolytelegrambot.config.game.GameConfig;
import ru.dabutskikh.monopolytelegrambot.dto.GameDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.entity.Game;
import ru.dabutskikh.monopolytelegrambot.entity.Player;
import ru.dabutskikh.monopolytelegrambot.entity.enums.GameStatus;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.repository.GameRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final PlayerService playerService;
    private final GameRepository gameRepository;
    private final PlayerGameService playerGameService;
    private final GameConfig gameConfig;

    @Transactional
    public GameDTO create(GameDTO dto) {
        Optional<PlayerDTO> playerOpt = playerService.findByTelegramId(dto.getOwnerTelegramId());
        if (playerOpt.isEmpty()) {
            throw new UserException("Вы не зарегистрированы!");
        }
        PlayerDTO player = playerOpt.get();
        if (player.getCurrentGameId() != null) {
            throw new UserException("Вы уже участвуете в игре с ID " + player.getCurrentGameId() + "!");
        }
        Game entity = new Game();
        entity.setOwner(new Player(player.getId()));
        entity.setStatus(GameStatus.CREATED);
        entity.setStartMoney(gameConfig.getStartMoney());
        entity.setForwardMoney(gameConfig.getForwardMoney());
        gameRepository.saveAndFlush(entity);
        PlayerGameDTO playerGame = playerGameService.create(
                PlayerGameDTO.builder()
                        .playerId(player.getId())
                        .gameId(entity.getId())
                        .build()
        );
        player.setCurrentGameId(entity.getId());
        player.setCurrentPlayerGameId(playerGame.getId());
        playerService.update(player);
        return GameDTO.builder()
                .id(entity.getId())
                .ownerTelegramId(player.getId())
                .build();
    }

    public void joinToGame(Long telegramId, Long gameId) {
        Optional<PlayerDTO> playerOpt = playerService.findByTelegramId(telegramId);
        if (playerOpt.isEmpty()) {
            throw new UserException("Вы не зарегистрированы!");
        }
        PlayerDTO player = playerOpt.get();
        if (player.getCurrentGameId() != null) {
            throw new UserException("Вы уже участвуете в игре с ID " + player.getCurrentGameId() + "!");
        }
        Optional<Game> gameOpt = gameRepository.findById(gameId);
        if (gameOpt.isEmpty()) {
            throw new UserException("Игры с ID " + gameId + " не существует!");
        }
        Game game = gameOpt.get();
        if (!GameStatus.CREATED.equals(game.getStatus())) {
            throw new UserException("Игра должна быть в статусе \"Создана\"!");
        }
        PlayerGameDTO playerGame = playerGameService.create(
                PlayerGameDTO.builder()
                        .playerId(player.getId())
                        .gameId(gameId)
                        .build()
        );
        player.setCurrentGameId(gameId);
        player.setCurrentPlayerGameId(playerGame.getId());
        playerService.update(player);
    }
}
