package ru.dabutskikh.monopolytelegrambot.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.dabutskikh.monopolytelegrambot.entity.enums.GameStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {

    Long ownerId;
    Long id;
    GameStatus status;
    Integer startMoney;
    Integer forwardMoney;
}
