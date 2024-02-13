package ru.dabutskikh.monopolytelegrambot.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.dabutskikh.monopolytelegrambot.entity.enums.GameStatus;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class GameDTO {

    Long ownerTelegramId;
    Long id;
    GameStatus status;
    BigDecimal startMoney;
    BigDecimal forwardMoney;
}
