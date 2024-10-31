package ru.dabutskikh.monopolytelegrambot.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;
import ru.dabutskikh.monopolytelegrambot.entity.enums.TxStatus;
import ru.dabutskikh.monopolytelegrambot.entity.enums.TxType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tx")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Tx {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tx_id")
    Long id;

    @Column(name = "tx_type")
    @Enumerated(EnumType.ORDINAL)
    TxType type;

    @Column(name = "tx_status")
    @Enumerated(EnumType.ORDINAL)
    TxStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_owner_id", referencedColumnName = "player_id")
    Player owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_source_id", referencedColumnName = "player_id")
    Player source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_target_id", referencedColumnName = "player_id")
    Player target;

    @Column(name = "tx_amount")
    Integer amount;

    @Column(name = "tx_updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;
}
