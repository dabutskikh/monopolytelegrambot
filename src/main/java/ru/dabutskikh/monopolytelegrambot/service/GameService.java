package ru.dabutskikh.monopolytelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public GameDTO create(GameDTO dto) {
        Optional<PlayerDTO> playerOpt = playerService.findByTelegramId(dto.getOwnerTelegramId());
        if (playerOpt.isEmpty()) {
            throw new UserException("Вы не зарегистрированы");
        }
        PlayerDTO player = playerOpt.get();
        if (player.getCurrentGameId() != null) {
            throw new UserException("Вы уже участвуете в игре с ID " + player.getCurrentGameId() + "!");
        }
        Game entity = new Game();
        entity.setOwner(new Player(player.getId()));
        entity.setStatus(GameStatus.CREATED);
        gameRepository.saveAndFlush(entity);
        PlayerGameDTO playerGameDTO = playerGameService.create(
                PlayerGameDTO.builder()
                        .playerId(player.getId())
                        .gameId(entity.getId())
                        .build()
        );
        player.setCurrentGameId(entity.getId());
        player.setCurrentPlayerGameId(playerGameDTO.getId());
        playerService.update(player);
        return GameDTO.builder()
                .id(entity.getId())
                .ownerTelegramId(player.getId())
                .build();
    }
}
