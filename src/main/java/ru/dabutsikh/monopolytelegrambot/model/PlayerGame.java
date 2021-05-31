package ru.dabutsikh.monopolytelegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "player_game")
@NoArgsConstructor
@Data
public class PlayerGame {

    @Embeddable
    @Data
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Id implements Serializable {
        @ManyToOne
        @JoinColumn(name = "player_id")
        private Player player;

        @ManyToOne
        @JoinColumn(name = "game_id")
        private Game game;
    }

    @EmbeddedId
    private Id id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PlayerGameStatus status;

    @Nullable
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private PlayerGameState state;

    @Nullable
    @Column(name = "current_money")
    private Integer currentMoney = 0;

    public PlayerGame(Player player, Game game) {
        this.id = new Id(player, game);
    }
}
