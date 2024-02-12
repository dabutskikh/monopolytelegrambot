package ru.dabutskikh.monopolytelegrambot.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class GameDTO {

    Long ownerTelegramId;
    Long id;
}
