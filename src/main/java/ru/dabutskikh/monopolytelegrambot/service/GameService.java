package ru.dabutskikh.monopolytelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dabutskikh.monopolytelegrambot.config.game.GameConfig;
import ru.dabutskikh.monopolytelegrambot.dto.GameDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.entity.Game;
import ru.dabutskikh.monopolytelegrambot.entity.Player;
import ru.dabutskikh.monopolytelegrambot.entity.enums.GameStatus;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.repository.GameRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final PlayerService playerService;
    private final GameRepository gameRepository;
    private final PlayerGameService playerGameService;
    private final GameConfig gameConfig;

    public GameDTO create(GameDTO dto) {
        Optional<PlayerDTO> playerOpt = playerService.findByTelegramId(dto.getOwnerId());
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
                .ownerId(player.getId())
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

    public GameDTO startGame(Long telegramId) {
        Optional<PlayerDTO> playerOpt = playerService.findByTelegramId(telegramId);
        if (playerOpt.isEmpty()) {
            throw new UserException("Вы не зарегистрированы!");
        }
        PlayerDTO owner = playerOpt.get();
        if (owner.getCurrentGameId() == null) {
            throw new UserException("В данный момент вы не состоите ни в одной игре!");
        }
        Optional<Game> gameOpt = gameRepository.findById(owner.getCurrentGameId());
        if (gameOpt.isEmpty()) {
            throw new UserException("Игра с ID " + owner.getCurrentGameId() + " не найдена!");
        }
        Game game = gameOpt.get();
        if (!GameStatus.CREATED.equals(game.getStatus())) {
            throw new UserException("Для старта игра должна быть в статусе \"Создана\"!");
        }
        if (!Objects.equals(game.getOwner().getId(), owner.getId())) {
            throw new UserException("Вы не являетесь создатаелем игры с ID " + game.getId() + "!");
        }
        List<PlayerDTO> players = playerService.findByCurrentGameId(game.getId());
        if (players.size() < 2) {
            throw new UserException("В игре должно быть не менее двух игроков!");
        }
        for (PlayerDTO player : players) {
            PlayerGameDTO playerGame = playerGameService.getById(player.getCurrentPlayerGameId());
            playerGame.setMoney(game.getStartMoney());
            playerGame.setState(PlayerGameState.DEFAULT);
            playerGameService.update(playerGame);
        }
        game.setStatus(GameStatus.IN_PROCESS);
        gameRepository.saveAndFlush(game);
        GameDTO gameDTO = new GameDTO();
        BeanUtils.copyProperties(game, gameDTO);
        return gameDTO;
    }

    public GameDTO getById(Long id) {
        Game entity = gameRepository.findById(id).orElseThrow(() -> new UserException("Записи с ID " + id + " не существует!"));
        GameDTO dto = new GameDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setOwnerId(entity.getOwner().getId());
        return dto;
    }
}
