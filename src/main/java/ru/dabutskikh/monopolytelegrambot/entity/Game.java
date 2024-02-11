package ru.dabutskikh.monopolytelegrambot.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.dabutskikh.monopolytelegrambot.entity.enums.GameStatus;

import javax.persistence.*;

@Entity
@Table(name = "game")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    Long id;

    @Column(name = "game_status")
    @Enumerated(EnumType.ORDINAL)
    GameStatus status;
}
