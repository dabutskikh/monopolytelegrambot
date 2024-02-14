package ru.dabutskikh.monopolytelegrambot.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameStatus;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerGameDTO {

    Long id;
    Long playerId;
    Long gameId;
    PlayerGameStatus status;
    PlayerGameState state;
    BigDecimal money;
}
