package ru.dabutskikh.monopolytelegrambot.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.dabutskikh.monopolytelegrambot.entity.enums.TxStatus;
import ru.dabutskikh.monopolytelegrambot.entity.enums.TxType;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TxDTO {

    Long id;
    Long gameId;
    TxType type;
    TxStatus status;
    Long ownerId;
    Long sourceId;
    Long targetId;
    Integer amount;
    LocalDateTime updatedAt;
}
