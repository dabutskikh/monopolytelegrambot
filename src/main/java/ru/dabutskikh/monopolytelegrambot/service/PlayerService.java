package ru.dabutskikh.monopolytelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerDTO;
import ru.dabutskikh.monopolytelegrambot.entity.Player;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.repository.PlayerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Transactional
    public void create(PlayerDTO dto) {
        checkNotExistsPlayerByTelegramId(dto.getTelegramId());
        Player entity = new Player();
        entity.setTelegramId(dto.getTelegramId());
        entity.setUsername(dto.getUsername());
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

    public PlayerDTO getById(Long id) {
        Player entity = playerRepository.findById(id).orElseThrow(() -> new UserException("Игрока с указанным идентификатором не существует"));
        PlayerDTO dto = new PlayerDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Transactional
    public PlayerDTO update(PlayerDTO dto) {
        Player entity = playerRepository.findById(dto.getId())
                .orElseThrow(() -> new UserException("Игрока с указанным идентификатором не существует"));
        BeanUtils.copyProperties(dto, entity);
        playerRepository.saveAndFlush(entity);
        return dto;
    }
}
