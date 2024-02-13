package ru.dabutskikh.monopolytelegrambot.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.dabutskikh.monopolytelegrambot.entity.enums.GameStatus;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "game")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_owner_id", referencedColumnName = "player_id")
    Player owner;

    @Column(name = "game_status")
    @Enumerated(EnumType.ORDINAL)
    GameStatus status;

    @Column(name = "game_start_money")
    BigDecimal startMoney;

    @Column(name = "game_forward_money")
    BigDecimal forwardMoney;

    public Game(Long id) {
        this.id = id;
    }
}
