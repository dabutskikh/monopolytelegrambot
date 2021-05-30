package ru.dabutsikh.monopolytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dabutsikh.monopolytelegrambot.model.Game;
import ru.dabutsikh.monopolytelegrambot.model.PlayerGame;

import java.util.List;

public interface PlayerGameRepository extends JpaRepository<PlayerGame, PlayerGame.Id> {

    @Query(value =
            "SELECT pg.* FROM player_game pg " +
            "INNER JOIN player p ON p.id = pg.player_id " +
            "INNER JOIN game g ON g.id = pg.game_id " +
            "WHERE p.id = ?1 " +
//            "    AND g.status IN ('CREATED', 'IN_PROCESS') " +
            "    AND g.status = 'IN_PROCESS' " +
            "    AND pg.status IN ('ACTIVE', 'SPECTATOR') " +
            "LIMIT 1",
            nativeQuery = true)
    List<PlayerGame> getProcessingGameByPlayerId(Long playerId);

    @Query(value =
            "SELECT pg.* FROM player_game pg " +
            "INNER JOIN player p ON p.id = pg.player_id " +
            "INNER JOIN game g ON g.id = pg.game_id " +
            "WHERE p.id = ?1 " +
            "    AND g.status = 'CREATED' " +
            "    AND pg.status IN ('ACTIVE', 'SPECTATOR') " +
            "LIMIT 1",
            nativeQuery = true)
    List<PlayerGame> getCreatedGameByPlayerId(Long playerId);

    @Query(value =
            "SELECT pg.* FROM player_game pg " +
            "INNER JOIN player p ON p.id = pg.player_id " +
            "INNER JOIN game g ON g.id = pg.game_id " +
            "WHERE p.id = ?1 " +
            "    AND g.status IN ('CREATED', 'IN_PROCESS') " +
            "    AND pg.status IN ('ACTIVE', 'SPECTATOR') " +
            "LIMIT 1",
            nativeQuery = true)
    List<PlayerGame> getCreatedOrProcessingGameByPlayerId(Long playerId);

    @Query(value =
            "SELECT * FROM player_game " +
            "WHERE game_id = ?1 AND player_id = ?2 " +
            "LIMIT 1",
            nativeQuery = true)
    List<PlayerGame> getPlayerGameByGameIdAndPlayerId(Long gameId, Long playerId);

    @Query(value =
            "SELECT * FROM player_game " +
            "WHERE game_id = ?1",
            nativeQuery = true)
    List<PlayerGame> getByGameId(Long gameId);

    @Query(value =
            "SELECT pg.* FROM game g " +
            "INNER JOIN player_game pg ON pg.game_id = g.id " +
            "WHERE g.id = ?1 AND pg.status = 'ACTIVE'",
            nativeQuery = true)
    List<PlayerGame> getActivePlayerGamesByGameId(Long gameId);
}
