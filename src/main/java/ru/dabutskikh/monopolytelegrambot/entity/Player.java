package ru.dabutskikh.monopolytelegrambot.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "player")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    Long id;

    @Column(name = "player_telegram_id")
    Long telegramId;

    @Column(name = "player_username")
    String username;

    @Column(name = "player_last_name")
    String lastName;

    @Column(name = "player_first_name")
    String firstName;

    @Column(name = "game_current_id")
    Long currentGameId;

    @Column(name = "player_game_current_id")
    Long currentPlayerGameId;

    @Column(name = "tx_current_id")
    Long currentTxId;

    public Player(Long id) {
        this.id = id;
    }
}
