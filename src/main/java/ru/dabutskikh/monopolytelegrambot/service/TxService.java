package ru.dabutskikh.monopolytelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dabutskikh.monopolytelegrambot.dto.PlayerGameDTO;
import ru.dabutskikh.monopolytelegrambot.dto.TxDTO;
import ru.dabutskikh.monopolytelegrambot.entity.Game;
import ru.dabutskikh.monopolytelegrambot.entity.Player;
import ru.dabutskikh.monopolytelegrambot.entity.PlayerGame;
import ru.dabutskikh.monopolytelegrambot.entity.Tx;
import ru.dabutskikh.monopolytelegrambot.entity.enums.TxStatus;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.repository.TxRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TxService {

    private final TxRepository txRepository;

    public Long create(TxDTO dto) {
        Tx entity = new Tx();
        entity.setGame(new Game(dto.getGameId()));
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : TxStatus.CREATED);
        entity.setOwner(new Player(dto.getOwnerId()));
        if (dto.getSourceId() != null) {
            entity.setSource(new Player(dto.getSourceId()));
        }
        if (dto.getTargetId() != null) {
            entity.setTarget(new Player(dto.getTargetId()));
        }
        entity.setAmount(dto.getAmount());
        entity.setUpdatedAt(LocalDateTime.now());
        txRepository.saveAndFlush(entity);
        return entity.getId();
    }

    public void update(TxDTO dto) {
        Tx entity = txRepository.findById(dto.getId())
                .orElseThrow(() -> new UserException("Записи с ID " + dto.getId() + " не существует!"));
        BeanUtils.copyProperties(dto, entity);
        entity.setOwner(new Player(dto.getOwnerId()));
        if (dto.getSourceId() != null) {
            entity.setSource(new Player(dto.getSourceId()));
        }
        if (dto.getTargetId() != null) {
            entity.setTarget(new Player(dto.getTargetId()));
        }
        entity.setUpdatedAt(LocalDateTime.now());
        txRepository.saveAndFlush(entity);
    }

    public TxDTO getById(Long id) {
        Tx entity = txRepository.findById(id).orElseThrow(() -> new UserException("Записи с ID " + id + " не существует!"));
        TxDTO dto = new TxDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setGameId(entity.getGame().getId());
        dto.setOwnerId(entity.getOwner().getId());
        if (entity.getSource() != null) {
            dto.setSourceId(entity.getSource().getId());
        }
        if (entity.getTarget() != null) {
            dto.setTargetId(entity.getTarget().getId());
        }
        return dto;
    }
}
