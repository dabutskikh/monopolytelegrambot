package ru.dabutskikh.monopolytelegrambot.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    Long id;
    Long telegramId;
    String username;
    String lastName;
    String firstName;
    Long currentGameId;
    Long currentPlayerGameId;
    Long currentTxId;
}
