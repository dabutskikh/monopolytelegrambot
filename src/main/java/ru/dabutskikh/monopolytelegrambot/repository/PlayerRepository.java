package ru.dabutskikh.monopolytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.dabutskikh.monopolytelegrambot.entity.Player;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByTelegramId(Long telegramId);

    List<Player> findByCurrentGameId(Long gameId);

    Optional<Player> findByUsername(String username);

    @Query("""
        select p
        from Player p
        inner join PlayerGame pg on pg.id = p.currentPlayerGameId
        where p.currentGameId = :gameId
            and pg.status = :status
    """)
    List<Player> findGamePlayersByStatus(Long gameId, PlayerGameStatus status);
}
