package ru.dabutsikh.monopolytelegrambot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "move")
@NoArgsConstructor
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private TransactionState state;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Player owner;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "payer_id")
    private Player payer;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Player recipient;

    @Nullable
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Nullable
    @Column(name = "amount")
    private Integer amount;

    @Nullable
    @Column(name = "date")
    private Date date;

    public Transaction(Game game, Player owner) {
        this.game = game;
        this.owner = owner;
    }
}
