package ru.dabutskikh.monopolytelegrambot.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class PlayerGameDTO {

    Long id;
    Long playerId;
    Long gameId;
    PlayerGameStatus status;
}
