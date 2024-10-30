package ru.dabutskikh.monopolytelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.entity.Player;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.repository.PlayerRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public void create(PlayerDTO dto) {
        checkNotExistsPlayerByTelegramId(dto.getTelegramId());
        Player entity = new Player();
        entity.setTelegramId(dto.getTelegramId());
        String telegramUsername = dto.getUsername() != null
                ? dto.getUsername()
                : String.valueOf(dto.getTelegramId());
        String username = Stream.of(dto.getFirstName(), dto.getLastName(), telegramUsername)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        entity.setUsername(username);
        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        playerRepository.saveAndFlush(entity);
    }

    public void checkNotExistsPlayerByTelegramId(Long telegramId) {
        Optional<Player> playerOpt = playerRepository.findByTelegramId(telegramId);
        if (playerOpt.isPresent()) {
            throw new UserException("Вы уже зарегистрированы!");
        }
    }

    public Optional<PlayerDTO> findByTelegramId(Long telegramId) {
        Optional<Player> playerOpt = playerRepository.findByTelegramId(telegramId);
        return playerOpt.map(player -> {
                    PlayerDTO dto = new PlayerDTO();
                    BeanUtils.copyProperties(player, dto);
                    return dto;
                }
        );
    }

    public PlayerDTO update(PlayerDTO dto) {
        Player entity = playerRepository.findById(dto.getId())
                .orElseThrow(() -> new UserException("Игрока с указанным идентификатором не существует"));
        BeanUtils.copyProperties(dto, entity);
        playerRepository.saveAndFlush(entity);
        return dto;
    }

    public List<PlayerDTO> findByCurrentGameId(Long gameId) {
        List<Player> entities = playerRepository.findByCurrentGameId(gameId);
        return entities.stream()
                .map(player -> {
                    PlayerDTO dto = new PlayerDTO();
                    BeanUtils.copyProperties(player, dto);
                    return dto;
                })
                .toList();
    }
}
