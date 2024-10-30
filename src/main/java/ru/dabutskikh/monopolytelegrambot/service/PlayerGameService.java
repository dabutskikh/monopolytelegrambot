package ru.dabutskikh.monopolytelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.entity.Game;
import ru.dabutskikh.monopolytelegrambot.entity.Player;
import ru.dabutskikh.monopolytelegrambot.entity.PlayerGame;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameStatus;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.repository.PlayerGameRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlayerGameService {

    private final PlayerGameRepository playerGameRepository;

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

    public PlayerGameDTO update(PlayerGameDTO dto) {
        PlayerGame entity = playerGameRepository.findById(dto.getId())
                .orElseThrow(() -> new UserException("Записи с ID " + dto.getId() + " не существует!"));
        BeanUtils.copyProperties(dto, entity);
        entity.setPlayer(new Player(dto.getPlayerId()));
        entity.setGame(new Game(dto.getGameId()));
        playerGameRepository.saveAndFlush(entity);
        return dto;
    }

    public PlayerGameDTO getById(Long id) {
        PlayerGame entity = playerGameRepository.findById(id).orElseThrow(() -> new UserException("Записи с ID " + id + " не существует!"));
        PlayerGameDTO dto = new PlayerGameDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setPlayerId(entity.getPlayer().getId());
        dto.setGameId(entity.getGame().getId());
        return dto;
    }
}
