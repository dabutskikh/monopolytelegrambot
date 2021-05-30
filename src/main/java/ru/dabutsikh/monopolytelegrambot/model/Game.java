package ru.dabutsikh.monopolytelegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "game")
@NoArgsConstructor
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private GameStatus status;

    @Column(name = "date_begin")
    @Nullable
    private Date dateBegin;

    @Column(name = "date_end")
    @Nullable
    private Date dateEnd;

    @Column(name = "start_money")
    private Integer startMoney;

    @Column(name = "forward_money")
    private Integer forwardMoney;

    @Column(name = "forward_money_time")
    private Integer forwardMoneyTime;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Player winner;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Player creator;

    @OneToMany(
            mappedBy = "game"
    )
    private List<Transaction> transactions;

    @OneToMany(
            mappedBy = "id.game"
    )
    private List<PlayerGame> playerGames;
}
