package ru.dabutskikh.monopolytelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dabutskikh.monopolytelegrambot.dto.TxDTO;
import ru.dabutskikh.monopolytelegrambot.entity.Game;
import ru.dabutskikh.monopolytelegrambot.entity.Player;
import ru.dabutskikh.monopolytelegrambot.entity.Tx;
import ru.dabutskikh.monopolytelegrambot.entity.enums.TxStatus;
import ru.dabutskikh.monopolytelegrambot.repository.TxRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TxService {

    private final TxRepository txRepository;

    public Long create(TxDTO txDto) {
        Tx entity = new Tx();
        entity.setGame(new Game(txDto.getGameId()));
        entity.setType(txDto.getType());
        entity.setStatus(txDto.getStatus() != null ? txDto.getStatus() : TxStatus.CREATED);
        entity.setOwner(new Player(txDto.getOwnerId()));
        if (txDto.getSourceId() != null) {
            entity.setSource(new Player(txDto.getSourceId()));
        }
        if (txDto.getTargetId() != null) {
            entity.setTarget(new Player(txDto.getTargetId()));
        }
        entity.setAmount(txDto.getAmount());
        entity.setUpdatedAt(LocalDateTime.now());
        txRepository.saveAndFlush(entity);
        return entity.getId();
    }
}
