package ru.dabutsikh.monopolytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dabutsikh.monopolytelegrambot.model.Player;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query(value =
            "SELECT p.* FROM player_game pg " +
            "INNER JOIN player p ON p.id = pg.player_id " +
            "INNER JOIN game g ON g.id = pg.game_id " +
            "WHERE g.id = ?1 AND pg.status != 'DISCONNECTED'",
            nativeQuery = true)
    List<Player> getConnectedPlayersByGameId(Long gameId);

    @Query(value =
            "SELECT p.* FROM player_game pg " +
                    "INNER JOIN player p ON p.id = pg.player_id " +
                    "INNER JOIN game g ON g.id = pg.game_id " +
                    "WHERE g.id = ?1 AND pg.status != 'ACTIVE'",
            nativeQuery = true)
    List<Player> getActivePlayersByGameId(Long gameId);
}
