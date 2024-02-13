package ru.dabutskikh.monopolytelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.entity.Game;
import ru.dabutskikh.monopolytelegrambot.entity.Player;
import ru.dabutskikh.monopolytelegrambot.entity.PlayerGame;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameStatus;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.repository.PlayerGameRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerGameService {

    private final PlayerGameRepository playerGameRepository;

    @Transactional
    public PlayerGameDTO create(PlayerGameDTO dto) {
        checkNotExistsPlayerGame(dto.getPlayerId(), dto.getGameId());
        PlayerGame entity = new PlayerGame();
        entity.setPlayer(new Player(dto.getPlayerId()));
        entity.setGame(new Game(dto.getGameId()));
        entity.setStatus(PlayerGameStatus.ACTIVE);
        playerGameRepository.saveAndFlush(entity);
        return PlayerGameDTO.builder()
                .id(entity.getId())
                .playerId(entity.getPlayer().getId())
                .gameId(entity.getGame().getId())
                .status(entity.getStatus())
                .build();
    }

    public void checkNotExistsPlayerGame(Long playerId, Long gameId) {
        Optional<PlayerGame> playerGameOpt = playerGameRepository.findByPlayerIdAndGameId(playerId, gameId);
        if (playerGameOpt.isPresent()) {
            throw new UserException("Вы уже состоите (состояли) в игре с ID " + gameId + "!");
        }
    }
}
