package ru.dabutsikh.monopolytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dabutsikh.monopolytelegrambot.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value =
            "SELECT m.* from move m " +
            "INNER JOIN game g ON g.id = m.game_id " +
            "INNER JOIN player_game pg ON pg.game_id = g.id " +
            "INNER JOIN player p ON p.id = pg.player_id " +
            "WHERE g.id = ?1 AND p.id = ?2 AND m.state = 'PREPARING'",
            nativeQuery = true)
    List<Transaction> getPreparingTransaction(Long gameId, Long ownerId);
}
