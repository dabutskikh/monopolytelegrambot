package ru.dabutskikh.monopolytelegrambot.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameStatus;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "player_game")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PlayerGame {

    @Id
    @Column(name = "player_game_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", referencedColumnName = "player_id")
    Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    Game game;

    @Column(name = "player_game_status")
    @Enumerated(EnumType.ORDINAL)
    PlayerGameStatus status;

    @Column(name = "player_game_state")
    @Enumerated(EnumType.ORDINAL)
    PlayerGameState state;

    @Column(name = "player_game_money")
    BigDecimal money;
}
